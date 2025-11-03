package com.example.microcapteur.Service;

import com.example.microcapteur.Entities.Mesure;
import com.example.microcapteur.Entities.TypeCapteur;

import java.util.List;

public interface MesureService {
    Mesure ajouterMesure(Mesure mesure, TypeCapteur typeCapteur);
    List<Mesure> getAllMesures();
    Mesure getMesureById(Long id);
    Mesure updateMesure(Long id, Mesure nouvelleMesure);
    void deleteMesure(Long id);

}
