package com.example.microcapteur.DTO;

import com.example.microcapteur.Entities.Mesure;
import com.example.microcapteur.Entities.TypeCapteur;

import java.util.List;

public class CapteurDTO {
    public String nom;
    public String localisation;
    public TypeCapteur type; // TEMPERATURE, H2, etc.
    public List<Mesure> mesures;     // cas 1 : mesures directes
    public List<Long> mesuresIds;    // cas 2 : mesures existantes
}
