package com.example.microcapteur.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data


@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Capteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    String nom;

    @Enumerated(EnumType.STRING)
    TypeCapteur type;

    String localisation;
    @OneToMany(  cascade = CascadeType.PERSIST , orphanRemoval = true)
    List<Mesure> mesures=new ArrayList<>();
    public Long getId() {
        return id;
    }




    // --- Getters & Setters ---

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TypeCapteur getType() {
        return type;
    }

    public void setType(TypeCapteur type) {
        this.type = type;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public List<Mesure> getMesures() {
        return mesures;
    }

    public void setMesures(List<Mesure> mesures) {
        this.mesures = mesures;
    }

}
