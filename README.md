# Kafka 指南项目

这是一个基于 Spring Boot 的 Kafka 示例项目，用于演示 Kafka 的基本使用方法。

## 技术栈

- Spring Boot 2.2.3.RELEASE
- Spring Kafka
- Java 8
- Maven

## 项目结构

```
src/main/java/com/example/kafkaguide/
├── KafkaGuideApplication.java
├── controller/
│   └── KafkaController.java
└── kafka/
    ├── KafkaProducer.java
    └── KafkaConsumer.java
```

## 如何运行

1. 确保已安装 Java 8
2. 确保 Kafka 服务器正在运行（默认地址：localhost:9092）
3. 在 IDEA 中打开项目
4. 运行 `KafkaGuideApplication` 类

## 测试 API

发送消息到 Kafka：

```bash
curl -X POST "http://localhost:8080/api/kafka/send?topic=test&key=testKey&message=Hello"
```

消息将被发送到指定的 topic，并且会被配置的消费者自动消费。

## 配置

主要配置文件位于 `src/main/resources/application.yml`：

- Kafka 服务器地址
- 消费者组 ID
- 生产者序列化配置
- 服务器端口等

## 注意事项

- 确保 Kafka 服务器已经启动并且可以访问
- 默认使用的 topic 是 "test"
- 消费者组 ID 默认为 "kafka-guide-group" 