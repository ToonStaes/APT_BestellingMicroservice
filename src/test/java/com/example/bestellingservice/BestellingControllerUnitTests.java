package com.example.bestellingservice;


import com.example.bestellingservice.model.Bestelling;
import com.example.bestellingservice.repository.BestellingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class BestellingControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BestellingRepository bestellingRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private ArrayList<String> gerechten1 = new ArrayList<String>();
    private ArrayList<String> gerechten2 = new ArrayList<String>();
    private ArrayList<String> gerechten3 = new ArrayList<String>();

    private Bestelling bestellingnNummer1Personeel1 = new Bestelling("1", "1", gerechten1);
    private Bestelling bestellingnNummer2Personeel1 = new Bestelling("2", "1", gerechten2);
    private Bestelling bestellingnNummer3Personeel2 = new Bestelling("3", "2", gerechten3);
    private Bestelling bestellingnNummer4Personeel3 = new Bestelling("4", "3", gerechten2);

    @BeforeEach
    public void beforeAllTests() {
        gerechten1.add("Pizza Margherita");
        gerechten1.add("Pizza Salami");
        gerechten1.add("Pizza Hawaii");
        gerechten2.add("Pizza Margherita");
        gerechten3.add("Pizza Salami");
    }

    @AfterEach
    public void afterAllTests() {
        gerechten1.clear();
        gerechten2.clear();
        gerechten3.clear();
    }

    @Test
    void givenBestelling_whenGetBestelinngen_thenReturnJsonReviews() throws Exception {
        List<Bestelling> bestellingList = new ArrayList<>();
        bestellingList.add(bestellingnNummer1Personeel1);
        bestellingList.add(bestellingnNummer2Personeel1);
        bestellingList.add(bestellingnNummer3Personeel2);
        bestellingList.add(bestellingnNummer4Personeel3);

        given(bestellingRepository.findAll()).willReturn(bestellingList);

        mockMvc.perform(get("/bestellingen"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].bestelNummer", is("1")))
                .andExpect(jsonPath("$[0].personeelsNummer", is("1")))
                .andExpect(jsonPath("$[0].gerechten", is(gerechten1)))
                .andExpect(jsonPath("$[1].bestelNummer", is("2")))
                .andExpect(jsonPath("$[1].personeelsNummer", is("1")))
                .andExpect(jsonPath("$[1].gerechten", is(gerechten2)))
                .andExpect(jsonPath("$[2].bestelNummer", is("3")))
                .andExpect(jsonPath("$[2].personeelsNummer", is("2")))
                .andExpect(jsonPath("$[2].gerechten", is(gerechten3)))
                .andExpect(jsonPath("$[3].bestelNummer", is("4")))
                .andExpect(jsonPath("$[3].personeelsNummer", is("3")))
                .andExpect(jsonPath("$[3].gerechten", is(gerechten2)));
    }

    @Test
    void givenBestelling_whenGetBestellingByBestelnummer_thenReturnJsonReview() throws Exception {
        given(bestellingRepository.findBestellingByBestelNummer(bestellingnNummer1Personeel1.getBestelNummer())).willReturn(bestellingnNummer1Personeel1);

        mockMvc.perform(get("/bestellingen/bestelnummer/{bestelNummer}", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personeelsNummer", is("1")))
                .andExpect(jsonPath("$.gerechten", is(gerechten1)));
    }

    @Test
    void givenBestelling_whenGetBestellingByPersoneelsNummer_thenReturnJsonReviews() throws Exception {
        List<Bestelling> bestellingList = new ArrayList<>();
        bestellingList.add(bestellingnNummer1Personeel1);
        bestellingList.add(bestellingnNummer2Personeel1);

        given(bestellingRepository.findBestellingByPersoneelsNummer("1")).willReturn(bestellingList);

        mockMvc.perform(get("/bestellingen/personeelsnummer/{personeelsNummer}", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].bestelNummer", is("1")))
                .andExpect(jsonPath("$[0].gerechten", is(gerechten1)))
                .andExpect(jsonPath("$[1].bestelNummer", is("2")))
                .andExpect(jsonPath("$[1].gerechten", is(gerechten2)));
    }

    @Test
    void whenPostBestelling_thenReturnJsonReview() throws Exception {
        Bestelling bestelling = new Bestelling("5", "3", gerechten1);

        mockMvc.perform(post("/bestellingen")
                .content(mapper.writeValueAsString(bestelling))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bestelNummer", is("5")))
                .andExpect(jsonPath("$.personeelsNummer", is("3")))
                .andExpect(jsonPath("$.gerechten", is(gerechten1)));
    }

    @Test
    void givenBestelling_whenPutBestelling_thenReturnJsonReview() throws Exception {
        given(bestellingRepository.findBestellingByBestelNummer("1")).willReturn(bestellingnNummer1Personeel1);

        Bestelling updatedBestelling = new Bestelling("1", "2", gerechten2);

        mockMvc.perform(put("/bestellingen")
                .content(mapper.writeValueAsString(updatedBestelling))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bestelNummer", is("1")))
                .andExpect(jsonPath("$.personeelsNummer", is("2")))
                .andExpect(jsonPath("$.gerechten", is(gerechten2)));
    }

    @Test
    void givenBestelling_whenDeleteBestelling_thenStatusOk() throws Exception {
        given(bestellingRepository.findBestellingByBestelNummer("4")).willReturn(bestellingnNummer4Personeel3);

        mockMvc.perform(delete("/bestellingen/bestelnummer/{bestelNummer}", "4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenNoBestelling_whenDeleteBestelling_thenStatusNotFound() throws Exception {
        given(bestellingRepository.findBestellingByBestelNummer("5")).willReturn(null);

        mockMvc.perform(delete("/bestellingen/bestelnummer/{bestelNummer}}", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
