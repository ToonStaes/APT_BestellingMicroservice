package com.example.bestellingservice;

import com.example.bestellingservice.model.Bestelling;
import com.example.bestellingservice.repository.BestellingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BestellingControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BestellingRepository bestellingRepository;

    private ArrayList<String> gerechten1 = new ArrayList<String>();
    private ArrayList<String> gerechten2 = new ArrayList<String>();
    private ArrayList<String> gerechten3 = new ArrayList<String>();

    private Bestelling bestellingnNummer1Personeel1 = new Bestelling("1", "1", gerechten1);
    private Bestelling bestellingnNummer2Personeel1 = new Bestelling("2", "1", gerechten2);
    private Bestelling bestellingnNummer3Personeel2 = new Bestelling("3", "2", gerechten3);
    private Bestelling bestellingnNummer4Personeel3 = new Bestelling("4", "3", gerechten2);

    @BeforeEach
    public void beforeAllTests() {
        bestellingRepository.deleteAll();
        gerechten1.add("20220103PM");
        gerechten1.add("20200103PS");
        gerechten1.add("20200103PH");
        gerechten2.add("20220103PM");
        gerechten3.add("20200103PS");
        bestellingRepository.save(bestellingnNummer1Personeel1);
        bestellingRepository.save(bestellingnNummer2Personeel1);
        bestellingRepository.save(bestellingnNummer3Personeel2);
        bestellingRepository.save(bestellingnNummer4Personeel3);
    }

    @AfterEach
    public void afterAllTests() {
        gerechten1.clear();
        gerechten2.clear();
        gerechten3.clear();
        bestellingRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void givenBestelling_whenGetBestellingen_thenReturnJsonReviews() throws Exception {
        List<Bestelling> bestellingList = new ArrayList<>();
        bestellingList.add(bestellingnNummer1Personeel1);
        bestellingList.add(bestellingnNummer2Personeel1);
        bestellingList.add(bestellingnNummer3Personeel2);
        bestellingList.add(bestellingnNummer4Personeel3);

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
    void givenBestelling_whenGetBestellingByBestelNummer_thenReturnJsonReview() throws Exception {
        mockMvc.perform(get("/bestellingen/bestelnummer/{bestelNummer}", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personeelsNummer", is("1")))
                .andExpect(jsonPath("$.gerechten", is(gerechten1)));
    }

    @Test
    void givenBestelling_whenGetBestellingByPersoneelsNummer_thenReturnJsonReviews() throws Exception {
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
    void givenBestelling_whenDeleteBestelling_thenStatusOk() throws Exception {
        mockMvc.perform(delete("/bestellingen/bestelnummer/{bestelNummer}", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenNoBestelling_whenDeleteBestelling_thenStatusNotFound() throws Exception {
        mockMvc.perform(delete("/bestellingen/bestelnummer/{bestelNummer}", "6")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenReview_whenPutBestelling_thenReturnJsonReview() throws Exception {
        Bestelling updatedBestelling = new Bestelling("1", "1", gerechten2);
        mockMvc.perform(put("/bestellingen")
                .content(mapper.writeValueAsString(updatedBestelling))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bestelNummer", is("1")))
                .andExpect(jsonPath("$.personeelsNummer", is("1")))
                .andExpect(jsonPath("$.gerechten", is(gerechten2)));
    }
}
