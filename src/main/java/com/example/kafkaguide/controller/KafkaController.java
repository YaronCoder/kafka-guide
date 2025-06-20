package com.example.kafkaguide.controller;

import com.example.kafkaguide.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/send")
    public String sendMessage(
            @RequestParam String topic,
            @RequestParam(required = false) String key,
            @RequestParam String message) {
        kafkaProducer.sendMessage(topic, key, message);
        return "Message sent successfully";
    }
}
