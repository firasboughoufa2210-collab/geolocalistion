package com.example.microcapteur.DTO;

import com.example.microcapteur.Entities.TypeCapteur;
import lombok.Data;

import java.util.List;

@Data

public class CapteurCompletDto {
    private Long id;
    private String nom;
    private TypeCapteur type;
    private String localisation;
    private List<MesureDTO> mesures;
    // Getters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public TypeCapteur getType() { return type; }
    public String getLocalisation() { return localisation; }
    public List<MesureDTO> getMesures() { return mesures; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setType(TypeCapteur type) { this.type = type; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public void setMesures(List<MesureDTO> mesures) { this.mesures = mesures; }
}