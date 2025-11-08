package com.example.microcapteur;

import com.example.microcapteur.DTO.CapteurCompletDto;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StationPublisher {
    @Autowired

    private  RabbitTemplate rabbitTemplate;

    public void sendStationToMs3(CapteurCompletDto dto) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend("station.exchange", "station.created", dto);
        System.out.println("📤 Sent station to MS3: " + dto.getNom());
    }
}
