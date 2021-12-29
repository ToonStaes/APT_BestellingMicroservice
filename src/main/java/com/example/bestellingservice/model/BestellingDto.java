package com.example.bestellingservice.model;

import java.util.List;

public class BestellingDto {
    private String bestelNummer;
    private String personeelsNummer;
    private List<String> gerechten;

    public String getBestelNummer() {
        return bestelNummer;
    }

    public String getPersoneelsNummer() {
        return personeelsNummer;
    }


    public List<String> getGerechten() {
        return gerechten;
    }

}
