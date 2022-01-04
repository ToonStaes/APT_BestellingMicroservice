package com.example.bestellingservice.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;

@Document(collection = "bestellingen")
public class Bestelling {
    @Id
    private String id;
    @Indexed(unique = true)
    private String bestelNummer;
    private String personeelsNummer;
    private List<String> gerechten;

    public Bestelling() {
    }

    public String getId() {
        return id;
    }

    public String getBestelNummer() {
        return bestelNummer;
    }

    public void setBestelNummer(String bestelNummer) {
        this.bestelNummer = bestelNummer;
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

    public Bestelling(String personeelsNummer, List<String> gerechten) {
        this.bestelNummer = generateBestelnummer(personeelsNummer, gerechten);
        this.personeelsNummer = personeelsNummer;
        this.gerechten = gerechten;
    }

    public String generateBestelnummer(String personeelsNummer, List<String> gerechten) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        String datestring = formatter.format(date);
        String bestelnummer = datestring +  personeelsNummer.substring(personeelsNummer.length() - 2);

        return bestelnummer;
    }
}
