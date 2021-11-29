package com.example.bestellingservice.controller;

import com.example.bestellingservice.model.Bestelling;
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

    @GetMapping("/bestellingen/{bestelNummer}")
    public Bestelling getBestellingByBestelNummer(@PathVariable String bestelNummer) {
        return bestellingRepository.findBestellingByBestelNummer(bestelNummer);
    }

    @GetMapping("/bestellingen/{personeelsnummer}")
    public List<Bestelling> getBestellingenByPersoneelsnummer(@PathVariable String personeelsnummer) {
        return bestellingRepository.findBestellingByPersoneelsnummer(personeelsnummer);
    }

    @PostMapping("/bestellingen")
    public Bestelling addBestelling(@RequestBody Bestelling bestelling) {
        bestellingRepository.save(bestelling);
        return bestelling;
    }

    @PutMapping("/bestellingen")
    public Bestelling updateBestelling(@RequestBody Bestelling updatedBestelling) {
        Bestelling retrievedBestelling = bestellingRepository.findBestellingByBestelNummerAndAndPersoneelsnummer(updatedBestelling.getBestelNummer(), updatedBestelling.getPersoneelsnummer());
        retrievedBestelling.setBestelNummer(updatedBestelling.getBestelNummer());
        retrievedBestelling.setPersoneelsnummer(updatedBestelling.getPersoneelsnummer());

        bestellingRepository.save((retrievedBestelling));

        return retrievedBestelling;
    }

    @DeleteMapping("/bestellingen/bestelnummer/{bestelNummer}/personeelsnummer/{personeelsNummer}")
    public ResponseEntity deleteBestelling(@PathVariable String bestelNummer, @PathVariable String personeelsNummer) {
        Bestelling bestelling = bestellingRepository.findBestellingByBestelNummerAndAndPersoneelsnummer(bestelNummer, personeelsNummer);
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
            List<String> gerechten = new ArrayList();
            gerechten.add("Pizza Margherita");
            gerechten.add("Pizza Salami");
            gerechten.add("Pizza Hawaii");
            bestellingRepository.save(new Bestelling("1", "2", gerechten, 120.49));
            bestellingRepository.save(new Bestelling("2", "2", gerechten, 120.49));
            bestellingRepository.save(new Bestelling("3", "1", gerechten, 120.49));
        }

        System.out.println("Bestelling test: " + bestellingRepository.findAll().size());
    }
}
