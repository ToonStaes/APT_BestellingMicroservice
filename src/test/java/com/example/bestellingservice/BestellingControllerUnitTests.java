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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    private Bestelling bestellingnNummer1Personeel1 = null;
    private Bestelling bestellingnNummer2Personeel1 = null;
    private Bestelling bestellingnNummer3Personeel2 = null;
    private Bestelling bestellingnNummer4Personeel3 = null;
    private String bestelnummer1 = null;
    private String bestelnummer2 = null;
    private String bestelnummer3 = null;
    private String bestelnummer4 = null;

    public String generateBestelnummer(String personeelsNummer) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        String datestring = formatter.format(date);
        UUID random = UUID.randomUUID();
        String bestelnummer = datestring +  personeelsNummer.substring(personeelsNummer.length() - 2) + random;

        return bestelnummer;
    }

    @BeforeEach
    public void beforeAllTests() throws InterruptedException {
        gerechten1.add("20220103PM");
        gerechten1.add("20200103PS");
        gerechten1.add("20200103PH");
        gerechten2.add("20220103PM");
        gerechten3.add("20200103PS");
        bestellingnNummer1Personeel1 = new Bestelling("K20220103AH", gerechten1);
        bestellingnNummer2Personeel1 = new Bestelling("K20220103AH", gerechten2);
        bestellingnNummer3Personeel2 = new Bestelling("K20220103TS", gerechten3);
        bestellingnNummer4Personeel3 = new Bestelling("Z20220103NV", gerechten2);
        bestellingnNummer1Personeel1.setBestelNummer(generateBestelnummer(bestellingnNummer1Personeel1.getPersoneelsNummer()));
        bestellingnNummer2Personeel1.setBestelNummer(generateBestelnummer(bestellingnNummer2Personeel1.getPersoneelsNummer()));
        bestellingnNummer3Personeel2.setBestelNummer(generateBestelnummer(bestellingnNummer3Personeel2.getPersoneelsNummer()));
        bestellingnNummer4Personeel3.setBestelNummer(generateBestelnummer(bestellingnNummer4Personeel3.getPersoneelsNummer()));
        bestelnummer1 = bestellingnNummer1Personeel1.getBestelNummer();
        bestelnummer2 = bestellingnNummer2Personeel1.getBestelNummer();
        bestelnummer3 = bestellingnNummer3Personeel2.getBestelNummer();
        bestelnummer4 = bestellingnNummer4Personeel3.getBestelNummer();
    }

    @AfterEach
    public void afterAllTests() {
        gerechten1.clear();
        gerechten2.clear();
        gerechten3.clear();
        bestellingRepository.deleteAll();
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
                .andExpect(jsonPath("$[0].bestelNummer", is(bestelnummer1)))
                .andExpect(jsonPath("$[0].personeelsNummer", is("K20220103AH")))
                .andExpect(jsonPath("$[0].gerechten", is(gerechten1)))
                .andExpect(jsonPath("$[1].bestelNummer", is(bestelnummer2)))
                .andExpect(jsonPath("$[1].personeelsNummer", is("K20220103AH")))
                .andExpect(jsonPath("$[1].gerechten", is(gerechten2)))
                .andExpect(jsonPath("$[2].bestelNummer", is(bestelnummer3)))
                .andExpect(jsonPath("$[2].personeelsNummer", is("K20220103TS")))
                .andExpect(jsonPath("$[2].gerechten", is(gerechten3)))
                .andExpect(jsonPath("$[3].bestelNummer", is(bestelnummer4)))
                .andExpect(jsonPath("$[3].personeelsNummer", is("Z20220103NV")))
                .andExpect(jsonPath("$[3].gerechten", is(gerechten2)));
    }

    @Test
    void givenBestelling_whenGetBestellingByBestelnummer_thenReturnJsonReview() throws Exception {
        given(bestellingRepository.findBestellingByBestelNummer(bestellingnNummer1Personeel1.getBestelNummer())).willReturn(bestellingnNummer1Personeel1);

        mockMvc.perform(get("/bestellingen/bestelnummer/{bestelNummer}", bestelnummer1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personeelsNummer", is("K20220103AH")))
                .andExpect(jsonPath("$.gerechten", is(gerechten1)));
    }

    @Test
    void givenBestelling_whenGetBestellingByPersoneelsNummer_thenReturnJsonReviews() throws Exception {
        List<Bestelling> bestellingList = new ArrayList<>();
        bestellingList.add(bestellingnNummer1Personeel1);
        bestellingList.add(bestellingnNummer2Personeel1);

        given(bestellingRepository.findBestellingByPersoneelsNummer("K20220103AH")).willReturn(bestellingList);

        mockMvc.perform(get("/bestellingen/personeelsnummer/{personeelsNummer}", "K20220103AH"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].bestelNummer", is(bestelnummer1)))
                .andExpect(jsonPath("$[0].gerechten", is(gerechten1)))
                .andExpect(jsonPath("$[1].bestelNummer", is(bestelnummer2)))
                .andExpect(jsonPath("$[1].gerechten", is(gerechten2)));
    }

    @Test
    void whenPostBestelling_thenReturnJsonReview() throws Exception {
        Bestelling bestelling = new Bestelling("Z20220103NV", gerechten1);

        mockMvc.perform(post("/bestellingen")
                .content(mapper.writeValueAsString(bestelling))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bestelNummer").isNotEmpty())
                .andExpect(jsonPath("$.personeelsNummer", is("Z20220103NV")))
                .andExpect(jsonPath("$.gerechten", is(gerechten1)));
    }

    @Test
    void givenBestelling_whenPutBestelling_thenReturnJsonReview() throws Exception {
        given(bestellingRepository.findBestellingByBestelNummer(bestelnummer1)).willReturn(bestellingnNummer1Personeel1);

        Bestelling updatedBestelling = bestellingnNummer1Personeel1;
        updatedBestelling.setPersoneelsNummer("K20220103TS");
        updatedBestelling.setGerechten(gerechten2);

        mockMvc.perform(put("/bestellingen")
                .content(mapper.writeValueAsString(updatedBestelling))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bestelNummer", is(bestelnummer1)))
                .andExpect(jsonPath("$.personeelsNummer", is("K20220103TS")))
                .andExpect(jsonPath("$.gerechten", is(gerechten2)));
    }

    @Test
    void givenBestelling_whenDeleteBestelling_thenStatusOk() throws Exception {
        given(bestellingRepository.findBestellingByBestelNummer(bestelnummer4)).willReturn(bestellingnNummer4Personeel3);

        mockMvc.perform(delete("/bestellingen/bestelnummer/{bestelNummer}", bestelnummer4)
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
