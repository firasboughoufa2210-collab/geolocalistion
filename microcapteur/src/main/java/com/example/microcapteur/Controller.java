package com.example.microcapteur;

import com.example.microcapteur.DTO.CapteurCompletDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")

public class Controller {
    @Autowired
    private StationPublisher stationPublisher;

    @PostMapping("/sendcapteur")
    public ResponseEntity<String> sendStation(@RequestBody CapteurCompletDto dto) {
        stationPublisher.sendStationToMs3(dto);
        return ResponseEntity.ok("Station envoyée !");
    }
}
