package com.example.bestellingservice.controller;

import com.example.bestellingservice.model.Bestelling;
import com.example.bestellingservice.model.BestellingDto;
import com.example.bestellingservice.repository.BestellingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BestellingController {
    @Autowired
    private BestellingRepository bestellingRepository;

    @GetMapping("/bestellingen")
    public List<Bestelling> getBestellingen() {
        return bestellingRepository.findAll();
    }

    @GetMapping("/bestellingen/bestelnummer/{bestelNummer}")
    public Bestelling getBestellingByBestelNummer(@PathVariable String bestelNummer) {
        return bestellingRepository.findBestellingByBestelNummer(bestelNummer);
    }

    @GetMapping("/bestellingen/personeelsnummer/{personeelsNummer}")
    public List<Bestelling> getBestellingenByPersoneelsNummer(@PathVariable String personeelsNummer) {
        return bestellingRepository.findBestellingByPersoneelsNummer(personeelsNummer);
    }

    @PostMapping("/bestellingen")
    public Bestelling addBestelling(@RequestBody BestellingDto bestellingDto) {
        Bestelling bestelling = new Bestelling();
        bestelling.setBestelNummer(bestellingDto.getBestelNummer());
        bestelling.setPersoneelsNummer(bestellingDto.getPersoneelsNummer());
        bestelling.setGerechten(bestellingDto.getGerechten());
        bestellingRepository.save(bestelling);
        return bestelling;
    }

    @PutMapping("/bestellingen")
    public Bestelling updateBestelling(@RequestBody BestellingDto updatedBestellingDto) {
        Bestelling bestelling = new Bestelling();
        bestelling.setBestelNummer(updatedBestellingDto.getBestelNummer());
        bestelling.setPersoneelsNummer(updatedBestellingDto.getPersoneelsNummer());
        bestelling.setGerechten(updatedBestellingDto.getGerechten());
        Bestelling retrievedBestelling = bestellingRepository.findBestellingByBestelNummer(bestelling.getBestelNummer());
        retrievedBestelling.setPersoneelsNummer(bestelling.getPersoneelsNummer());
        retrievedBestelling.setGerechten(bestelling.getGerechten());

        bestellingRepository.save((retrievedBestelling));

        return retrievedBestelling;
    }

    @DeleteMapping("/bestellingen/bestelnummer/{bestelNummer}")
    public ResponseEntity<String> deleteBestelling(@PathVariable String bestelNummer) {
        Bestelling bestelling = bestellingRepository.findBestellingByBestelNummer(bestelNummer);
        if (bestelling!=null) {
            bestellingRepository.delete(bestelling);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostConstruct
    public void fillDB() {
        bestellingRepository.deleteAll();
        if(bestellingRepository.count() == 0) {
            List<String> gerechten = new ArrayList<>();
            gerechten.add("20220103PM");
            gerechten.add("20220103PH");
            gerechten.add("20220103PS");
            bestellingRepository.save(new Bestelling("1", "K20220103AH", gerechten));
            bestellingRepository.save(new Bestelling("2", "K20220103TS", gerechten));
            bestellingRepository.save(new Bestelling("3", "Z20220103NV", gerechten));
        }
    }
}
