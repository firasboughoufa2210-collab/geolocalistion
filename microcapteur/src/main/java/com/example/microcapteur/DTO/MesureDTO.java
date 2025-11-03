package com.example.microcapteur.DTO;

import com.example.microcapteur.Entities.Mesure;
import com.example.microcapteur.Entities.TypeCapteur;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class MesureDTO {

    @JsonProperty("valeur")
    private Double valeur;  // Utiliser Double plutôt que double

    @JsonProperty("unite")
    private String unite;

    @JsonProperty("dateHeure")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateHeure;

    @JsonProperty("typeCapteur")
    private TypeCapteur typeCapteur;

    // Convertir DTO -> Entité
    public Mesure toMesure() {
        Mesure m = new Mesure();
        m.setValeur(this.valeur);
        m.setUnite(this.unite);
        m.setDateHeure(this.dateHeure);
        return m;
    }

    // Getters et Setters
    public Double getValeur() { return valeur; }
    public void setValeur(Double valeur) { this.valeur = valeur; }

    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public TypeCapteur getTypeCapteur() { return typeCapteur; }
    public void setTypeCapteur(TypeCapteur typeCapteur) { this.typeCapteur = typeCapteur; }
}
