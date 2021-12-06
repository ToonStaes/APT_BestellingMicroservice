package com.example.bestellingservice.repository;

import com.example.bestellingservice.model.Bestelling;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BestellingRepository extends MongoRepository<Bestelling, String> {
    Bestelling findBestellingByBestelNummer(String bestelNummer);
    List<Bestelling> findBestellingByPersoneelsNummer(String personeelsNummer);
}
