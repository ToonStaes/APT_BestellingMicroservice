package com.example.bestellingservice.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "bestellingen")
public class Bestelling {
    @Id
    private String id;
    private String bestelNummer;
    private String personeelsnummer;
    private List<String> gerechten;
    private double totaalPrijs;

    public Bestelling(String bestelNummer, String personeelsnummer, List<String> gerechten, double totaalPrijs) {
        this.bestelNummer = bestelNummer;
        this.personeelsnummer = personeelsnummer;
        this.gerechten = gerechten;
        this.totaalPrijs = totaalPrijs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBestelNummer() {
        return bestelNummer;
    }

    public void setBestelNummer(String bestelNummer) {
        this.bestelNummer = bestelNummer;
    }

    public String getPersoneelsnummer() {
        return personeelsnummer;
    }

    public void setPersoneelsnummer(String personeelsnummer) {
        this.personeelsnummer = personeelsnummer;
    }

    public List<String> getGerechten() {
        return gerechten;
    }

    public void setGerechten(List<String> gerechten) {
        this.gerechten = gerechten;
    }

    public double getTotaalPrijs() {
        return totaalPrijs;
    }

    public void setTotaalPrijs(double totaalPrijs) {
        this.totaalPrijs = totaalPrijs;
    }
}
