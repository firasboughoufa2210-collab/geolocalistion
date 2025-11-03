package com.example.microcapteur.Service;


import com.example.microcapteur.Entities.Mesure;
import com.example.microcapteur.Entities.TypeCapteur;
import com.example.microcapteur.Repository.MesureRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.microcapteur.Entities.TypeCapteur.*;

@Service
@Transactional
@AllArgsConstructor

public class MesureServiceImpl implements MesureService {
    @Autowired
    private MesureRepository mesureRepository;

    public Mesure ajouterMesure(Mesure mesure, TypeCapteur typeCapteur) {
        // Vérification de la date : non vide et pas dans le futur
        if (mesure.getDateHeure() == null) {
            throw new IllegalArgumentException("La date de la mesure ne peut pas être vide !");
        }
        if (mesure.getDateHeure().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de la mesure ne peut pas être dans le futur !");
        }

        // Vérification de l'unité
        if (mesure.getUnite() == null || mesure.getUnite().isEmpty()) {
            throw new IllegalArgumentException("L'unité de la mesure doit être renseignée");
        }

        // Contrôle de la valeur selon le type de capteur
        switch (typeCapteur) {
            case TEMPERATURE:
                // La température peut être négative
                break;
            case HUMIDITE:
            case CO2:
            case H2:
            case PM2_5:
            case PM10:
            case PLUIE:
            case VENT:
            case LUMIERE:
                if (mesure.getValeur() < 0) {
                    throw new IllegalArgumentException("La valeur doit être positive pour ce type de capteur : " + typeCapteur);
                }
                break;
        }

        return mesureRepository.save(mesure);
    }


    public List<Mesure> getAllMesures() {
        return mesureRepository.findAll();
    }

    // ✅ Récupérer une mesure par ID
    public Mesure getMesureById(Long id) {
        return mesureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesure introuvable avec id : " + id));
    }

    // ✅ Mettre à jour une mesure
    public Mesure updateMesure(Long id, Mesure nouvelleMesure) {
        Mesure mesure = getMesureById(id);
        mesure.setValeur(nouvelleMesure.getValeur());
        mesure.setUnite(nouvelleMesure.getUnite());
        mesure.setDateHeure(nouvelleMesure.getDateHeure());
        return mesureRepository.save(mesure);
    }


    // ✅ Supprimer une mesure
    public void deleteMesure(Long id) {
        mesureRepository.deleteById(id);
    }

}

