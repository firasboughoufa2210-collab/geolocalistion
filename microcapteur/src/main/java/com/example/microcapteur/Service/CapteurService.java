package com.example.microcapteur.Service;

import com.example.microcapteur.DTO.CapteurDTO;
import com.example.microcapteur.Entities.Capteur;
import com.lowagie.text.DocumentException;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface CapteurService {
    Capteur ajouterCapteur(CapteurDTO capteurDTO);
    List<Capteur> getAllCapteurs();
    Capteur getCapteurById(Long id);
    void deleteCapteur(Long id);
    Capteur updateCapteur(Long id, CapteurDTO capteurDTO);
    ByteArrayInputStream generatePdf(Capteur capteur) throws DocumentException;
    List<Capteur> getCapteursByLocalisation(String localisation);

}
