package com.example.kafkaguide.consumer;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class NativeKafkaConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final AtomicBoolean running = new AtomicBoolean(true);
    
    public NativeKafkaConsumer(String bootstrapServers, String groupId) {
        // 创建消费者配置
        Properties props = new Properties();
        // 基础配置
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "native-consumer-" + groupId);
        
        // 反序列化器配置
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        
        // 消费者性能调优配置
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "1");
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "500");
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "1048576");
        
        // 消费者偏移量配置
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");
        
        // 创建消费者实例
        this.consumer = new KafkaConsumer<>(props);
    }
    
    public void subscribe(String topic) {
        // 订阅主题
        consumer.subscribe(Lists.newArrayList(topic));
        
        // 启动消费线程
        Thread consumerThread = new Thread(() -> {
            try {
                while (running.get()) {
                    // 拉取消息，超时时间设置为1秒
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                    
                    // 处理消息
                    for (ConsumerRecord<String, String> record : records) {
                        processMessage(record);
                    }
                    
                    // 手动提交偏移量
                    if (!records.isEmpty()) {
                        consumer.commitAsync((offsets, exception) -> {
                            if (exception != null) {
                                log.error("Commit failed for offsets {}", offsets, exception);
                            } else {
                                log.debug("Commit succeeded for offsets {}", offsets);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                log.error("Error while consuming messages", e);
            } finally {
                try {
                    // 最后一次同步提交，确保偏移量更新
                    consumer.commitSync();
                } finally {
                    consumer.close();
                }
            }
        });
        
        // 设置为守护线程
        consumerThread.setDaemon(true);
        consumerThread.start();
    }
    
    private void processMessage(ConsumerRecord<String, String> record) {
        log.info("Received message: topic = {}, partition = {}, offset = {}, key = {}, value = {}",
                record.topic(),
                record.partition(),
                record.offset(),
                record.key(),
                record.value());
        
        // TODO: 在这里添加您的业务处理逻辑
    }
    
    public void shutdown() {
        running.set(false);
    }
    
    // 使用示例
    public static void main(String[] args) {
        NativeKafkaConsumer consumer = new NativeKafkaConsumer("localhost:9092", "native-consumer-group");
        consumer.subscribe("test");
        
        // 添加关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::shutdown));
    }
}
