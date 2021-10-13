package com.example.bestellingservice.controller;

import com.example.bestellingservice.model.Bestelling;
import com.example.bestellingservice.repository.BestellingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

    @PostConstruct
    public void fillDB() {
        if(bestellingRepository.count() == 0) {
            List<String> gerechten = new ArrayList();
            gerechten.add("Pizza Margherita");
            gerechten.add("Pizza Salami");
            gerechten.add("Pizza Hawaii");
            bestellingRepository.save(new Bestelling("1", "2", gerechten, 120.49));
        }

        System.out.println("Bestelling test: " + bestellingRepository.findAll().size());
    }
}
