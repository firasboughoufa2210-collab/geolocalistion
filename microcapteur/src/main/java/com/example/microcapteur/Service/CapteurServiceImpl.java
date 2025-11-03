package com.example.microcapteur.Service;

import com.example.microcapteur.DTO.CapteurDTO;
import com.example.microcapteur.Entities.Capteur;
import com.example.microcapteur.Entities.Mesure;
import com.example.microcapteur.Entities.TypeCapteur;
import com.example.microcapteur.Repository.CapteurRepository;
import com.example.microcapteur.Repository.MesureRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor

public class CapteurServiceImpl implements CapteurService {
    @Autowired
    private CapteurRepository capteurRepository;
    @Autowired
    private MesureRepository mesureRepository;

    public Capteur ajouterCapteur(CapteurDTO capteurDTO) {
        // --- Contrôles de base ---
        if (capteurDTO.nom == null || capteurDTO.nom.isEmpty()) {
            throw new IllegalArgumentException("Le nom du capteur doit être renseigné");
        }
        if (capteurDTO.localisation == null || capteurDTO.localisation.isEmpty()) {
            throw new IllegalArgumentException("La localisation du capteur doit être renseignée");
        }
        if (capteurDTO.type == null) {
            throw new IllegalArgumentException("Le type de capteur doit être renseigné");
        }

        Capteur capteur = new Capteur();
        capteur.setNom(capteurDTO.nom);
        capteur.setLocalisation(capteurDTO.localisation);
        capteur.setType(capteurDTO.type);

        // --- Cas 1 : mesures directes ---
        if (capteurDTO.mesures != null) {
            for (Mesure m : capteurDTO.mesures) {
                verifierValeurMesure(capteurDTO.type, m);
                capteur.getMesures().add(m);
            }
        }

        // --- Cas 2 : mesures existantes par ID ---
        if (capteurDTO.mesuresIds != null) {
            List<Mesure> mesuresExistantes = mesureRepository.findAllById(capteurDTO.mesuresIds);
            for (Mesure m : mesuresExistantes) {
                verifierValeurMesure(capteurDTO.type, m);
                capteur.getMesures().add(m);
            }
        }

        return capteurRepository.save(capteur);
    }

    // --- Méthode utilitaire pour vérifier une mesure ---
    private void verifierValeurMesure(TypeCapteur type, Mesure m) {
        if (!autoriseValeurNegative(type, m.getUnite()) && m.getValeur() < 0) {
            throw new IllegalArgumentException(
                    "Valeur négative interdite pour le type " + type + " et l'unité " + m.getUnite()
            );
        }
        if (m.getUnite() == null || m.getUnite().isEmpty()) {
            throw new IllegalArgumentException("L'unité de la mesure doit être renseignée");
        }
        if (m.getDateHeure() == null || m.getDateHeure().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de la mesure ne peut pas être dans le futur !");
        }
    }

    // --- Map statique pour configurer les types/unités autorisés négatifs ---
    private static final Map<TypeCapteur, Set<String>> NEGATIF_AUTORISE = new HashMap<>();
    static {
        NEGATIF_AUTORISE.put(TypeCapteur.TEMPERATURE, new HashSet<>(Arrays.asList("°C", "K")));
        NEGATIF_AUTORISE.put(TypeCapteur.H2, new HashSet<>()); // aucune unité négative autorisée
        // tu peux ajouter d'autres capteurs si besoin
    }

    // --- Méthode utilitaire pour savoir si valeur négative autorisée ---
    private boolean autoriseValeurNegative(TypeCapteur type, String unite) {
        return NEGATIF_AUTORISE.containsKey(type) && NEGATIF_AUTORISE.get(type).contains(unite);
    }















    // 🔍 Lire tous les capteurs
    public List<Capteur> getAllCapteurs() {
        return capteurRepository.findAll();
    }

    // 🔍 Lire un capteur par ID
    public Capteur getCapteurById(Long id) {
        return capteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Capteur non trouvé avec id = " + id));
    }

    // ❌ Supprimer un capteur
    public void deleteCapteur(Long id) {
        capteurRepository.deleteById(id);
    }

    public Capteur updateCapteur(Long id, CapteurDTO capteurDTO) {
        Capteur ancien = getCapteurById(id);

        // --- Mise à jour des champs simples ---
        ancien.setNom(capteurDTO.nom);
        ancien.setLocalisation(capteurDTO.localisation);
        ancien.setType(capteurDTO.type);

        // --- Mettre à jour ou ajouter les mesures ---
        if (capteurDTO.mesures != null) {
            for (Mesure m : capteurDTO.mesures) {
                verifierMesure(m, capteurDTO.type);

                if (m.getId() != null) {
                    // Mesure existante → mise à jour
                    Mesure existante = mesureRepository.findById(m.getId())
                            .orElseThrow(() -> new RuntimeException("Mesure non trouvée: " + m.getId()));
                    existante.setValeur(m.getValeur());
                    existante.setUnite(m.getUnite());
                    existante.setDateHeure(m.getDateHeure());
                } else {
                    // Nouvelle mesure → sauvegarder et ajouter
                    Mesure nouvelle = mesureRepository.save(m);
                    ancien.getMesures().add(nouvelle);
                }
            }
        }

        // --- Sauvegarde finale ---
        return capteurRepository.save(ancien);
    }

    // --- Vérification mesure ---
    private void verifierMesure(Mesure m, TypeCapteur type) {
        Objects.requireNonNull(m.getValeur(), "La valeur de la mesure doit être renseignée");
        Objects.requireNonNull(m.getUnite(), "L'unité de la mesure doit être renseignée");
        Objects.requireNonNull(m.getDateHeure(), "La date de la mesure doit être renseignée");

        switch (type) {
            case TEMPERATURE, H2 -> { /* valeurs négatives autorisées */ }
            default -> {
                if (m.getValeur() < 0) throw new IllegalArgumentException(
                        "La valeur doit être positive pour le type " + type
                );
            }
        }

        if (m.getDateHeure().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de la mesure ne peut pas être dans le futur !");
        }
    }




    /// ////////////////////

    public ByteArrayInputStream generatePdf(Capteur capteur) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // --- Titre ---
        Font fontTitre = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph titre = new Paragraph("Fiche Capteur: " + capteur.getNom(), fontTitre);
        titre.setAlignment(Element.ALIGN_CENTER);
        titre.setSpacingAfter(20);
        document.add(titre);

        // --- Infos Capteur ---
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);
        document.add(new Paragraph("ID : " + capteur.getId(), fontNormal));
        document.add(new Paragraph("Type : " + capteur.getType(), fontNormal));
        document.add(new Paragraph("Localisation : " + capteur.getLocalisation(), fontNormal));
        document.add(new Paragraph(" ")); // ligne vide

        // --- Tableau des mesures ---
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 2, 2, 2});

        // En-têtes
        Stream.of("ID", "Valeur", "Unité", "Date/Heure")
                .forEach(headerTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(Color.LIGHT_GRAY);
                    header.setPhrase(new Phrase(headerTitle));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        // Contenu
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Mesure m : capteur.getMesures()) {
            table.addCell(String.valueOf(m.getId()));
            table.addCell(String.valueOf(m.getValeur()));
            table.addCell(m.getUnite());
            table.addCell(m.getDateHeure().format(formatter));
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }





    // --- Lire tous les capteurs par localisation ---
    public List<Capteur> getCapteursByLocalisation(String localisation) {
        return capteurRepository.findByLocalisation(localisation);
    }









}
