package com.example.microcapteur;

import com.example.microcapteur.DTO.CapteurDTO;
import com.example.microcapteur.DTO.MesureDTO;
import com.example.microcapteur.Entities.Capteur;
import com.example.microcapteur.Entities.Mesure;
import com.example.microcapteur.Service.CapteurService;
import com.example.microcapteur.Service.MesureService;
import com.lowagie.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping ("/api")

public class IOTController {
    @Autowired
    private   MesureService mesureService;
    @Autowired
    private CapteurService capteurService;

    // Ajouter une mesure
    @PostMapping("/addmesure")
    public Mesure ajouterMesure(@RequestBody MesureDTO mesureDTO) {
        if (mesureDTO.getTypeCapteur() == null) {
            throw new IllegalArgumentException("Le type de capteur doit être renseigné");
        }
        Mesure mesure = mesureDTO.toMesure();
        return mesureService.ajouterMesure(mesure, mesureDTO.getTypeCapteur());
    }

    // ✅ Récupérer toutes les mesures
    @GetMapping("/allmesure")
    public List<Mesure> getAllMesures() {
        return mesureService.getAllMesures();
    }

    // ✅ Récupérer une mesure par ID
    @GetMapping("/mesure/{id}")
    public Mesure getMesureById(@PathVariable Long id) {
        return mesureService.getMesureById(id);

    }
    // ✅ Mettre à jour une mesure
    @PutMapping("/updatemesure/{id}")
    public Mesure updateMesure(@PathVariable Long id, @RequestBody Mesure nouvelleMesure) {
        return mesureService.updateMesure(id, nouvelleMesure);
    }

    // ✅ Supprimer une mesure
    @DeleteMapping("/deletemesure/{id}")
    public String deleteMesure(@PathVariable Long id) {
        mesureService.deleteMesure(id);
        return "Mesure supprimée avec succès ✅";
    }





    // --- Ajouter un capteur (avec mesures directes ou existantes) ---
    @PostMapping("/capteurs")
    public ResponseEntity<Capteur> ajouterCapteur(@RequestBody CapteurDTO capteurDTO) {
        Capteur capteur = capteurService.ajouterCapteur(capteurDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(capteur);
    }

    @GetMapping("/allcapteur")
    public List<Capteur> getAllCapteurs() {
        return capteurService.getAllCapteurs();
    }

    @GetMapping("/capteur/{id}")
    public Capteur getCapteurById(@PathVariable Long id) {
        return capteurService.getCapteurById(id);
    }


    // 🔄 Mettre à jour un capteur existant
    @PutMapping("/capteur/{id}")
    public Capteur updateCapteur(@PathVariable Long id, @RequestBody CapteurDTO capteurDTO) {
        return capteurService.updateCapteur(id, capteurDTO);
    }

    // ❌ Supprimer un capteur
    @DeleteMapping("/capteur/{id}")
    public ResponseEntity<String> deleteCapteur(@PathVariable Long id) {
        capteurService.deleteCapteur(id);
        return ResponseEntity.ok("Capteur supprimé avec succès !");
    }


    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getCapteurPdf(@PathVariable Long id) throws DocumentException {
        Capteur capteur = capteurService.getCapteurById(id);
        ByteArrayInputStream bis = capteurService.generatePdf(capteur);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=capteur_" + id + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }


    // GET /api/capteur/localisation/Sousse
    @GetMapping("/localisation/{localisation}")
    public List<Capteur> getCapteursByLocalisation(@PathVariable String localisation) {
        return capteurService.getCapteursByLocalisation(localisation);
    }







}
