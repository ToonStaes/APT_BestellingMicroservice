package com.example.bestellingservice.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "bestellingen")
public class Bestelling {
    @Id
    private String id;
    private String bestelNummer;
    private String personeelsNummer;
    private List<String> gerechten;

    public Bestelling(String bestelNummer, String personeelsNummer, List<String> gerechten) {
        this.bestelNummer = bestelNummer;
        this.personeelsNummer = personeelsNummer;
        this.gerechten = gerechten;
    }

    public String getId() {
        return id;
    }

    public String getBestelNummer() {
        return bestelNummer;
    }

    public String getPersoneelsNummer() {
        return personeelsNummer;
    }

    public void setPersoneelsNummer(String personeelsNummer) {
        this.personeelsNummer = personeelsNummer;
    }

    public List<String> getGerechten() {
        return gerechten;
    }

    public void setGerechten(List<String> gerechten) {
        this.gerechten = gerechten;
    }
}
