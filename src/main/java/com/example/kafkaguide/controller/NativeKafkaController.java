package com.example.kafkaguide.controller;

import com.example.kafkaguide.consumer.NativeKafkaConsumer;
import com.example.kafkaguide.producer.NativeKafkaProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@RestController
@RequestMapping("/api/native-kafka")
public class NativeKafkaController {

    private NativeKafkaProducer nativeKafkaProducer;
    private NativeKafkaConsumer nativeKafkaConsumer;

    @PostConstruct
    public void init() {
        nativeKafkaProducer = new NativeKafkaProducer();
        nativeKafkaConsumer = new NativeKafkaConsumer("localhost:9092", "native-consumer-group");
    }

    @PostMapping("/send")
    public String sendMessage(
            @RequestParam String topic,
            @RequestParam(required = false) String key,
            @RequestParam String message) {
        nativeKafkaProducer.asyncSendMessage(topic, key, message);
        return "消息已发送";
    }

    @PreDestroy
    public void destroy() {
        if (nativeKafkaProducer != null) {
            nativeKafkaProducer.close();
        }
    }
}
