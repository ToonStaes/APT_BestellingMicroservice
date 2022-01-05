package com.example.bestellingservice.controller;

import com.example.bestellingservice.model.Bestelling;
import com.example.bestellingservice.model.BestellingDto;
import com.example.bestellingservice.repository.BestellingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
        bestelling.setBestelNummer(generateBestelnummer(bestellingDto.getPersoneelsNummer()));
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
            gerechten.add("20200103PH");
            gerechten.add("20200103PS");
            Bestelling bestellingArne1 = new Bestelling("K20220103AH", gerechten);
            Bestelling bestellingArne2 = new Bestelling("K20220103AH", gerechten);
            Bestelling bestellingToon = new Bestelling("K20220103TS", gerechten);
            Bestelling bestellingNiels = new Bestelling("Z20220103NV", gerechten);
            bestellingArne1.setBestelNummer(generateBestelnummer(bestellingArne1.getPersoneelsNummer()));
            bestellingArne2.setBestelNummer(generateBestelnummer(bestellingArne2.getPersoneelsNummer()));
            bestellingToon.setBestelNummer(generateBestelnummer(bestellingToon.getPersoneelsNummer()));
            bestellingNiels.setBestelNummer(generateBestelnummer(bestellingNiels.getPersoneelsNummer()));
            bestellingRepository.save(bestellingArne1);
            bestellingRepository.save(bestellingArne2);
            bestellingRepository.save(bestellingToon);
            bestellingRepository.save(bestellingNiels);
        }
    }

    public String generateBestelnummer(String personeelsNummer) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        String datestring = formatter.format(date);
        UUID random = UUID.randomUUID();
        String bestelnummer = datestring +  personeelsNummer.substring(personeelsNummer.length() - 2) + random;

        return bestelnummer;
    }
}
