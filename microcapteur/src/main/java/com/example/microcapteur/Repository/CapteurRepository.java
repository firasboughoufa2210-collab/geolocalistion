package com.example.microcapteur.Repository;

import com.example.microcapteur.Entities.Capteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CapteurRepository extends JpaRepository<Capteur, Long> {
    // Méthode pour chercher par localisation
    List<Capteur> findByLocalisation(String localisation);
}
