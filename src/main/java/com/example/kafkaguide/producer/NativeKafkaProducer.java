package com.example.kafkaguide.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class NativeKafkaProducer {

    private final Producer<String, String> producer;

    public NativeKafkaProducer() {
        Properties kafkaProps = new Properties();
        // 设置 Kafka 服务器地址
        kafkaProps.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        // 设置序列化器
        kafkaProps.put("key.serializer", StringSerializer.class.getName());
        kafkaProps.put("value.serializer", StringSerializer.class.getName());
        kafkaProps.put("max.in.flight.requests.per.connection", 6);
        kafkaProps.put("enable.idempotence", true);

        // 创建生产者实例
        this.producer = new KafkaProducer<>(kafkaProps);
    }

    /**
     * 异步发送消息
     * @param topic
     * @param key
     * @param value
     */
    public void asyncSendMessage(String topic, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        try {
            // 异步发送消息
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("发送消息失败: " + exception.getMessage());
                } else {
                    System.out.println("消息发送成功: topic = " + metadata.topic() +
                            ", partition = " + metadata.partition() +
                            ", offset = " + metadata.offset());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步发送消息
     * @param topic
     * @param key
     * @param value
     */
    public void syncSendMessage(String topic, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        try {
            // 同步步发送消息,使用 get() 方法等待发送完成
            producer.send(record).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        producer.close();
    }

    // 示例：如何使用自定义对象作为消息值
    public static class CustomRecord {
        private String customerCountry;
        private String productName;

        public CustomRecord(String customerCountry, String productName) {
            this.customerCountry = customerCountry;
            this.productName = productName;
        }

        // getter 和 setter
    }
}
