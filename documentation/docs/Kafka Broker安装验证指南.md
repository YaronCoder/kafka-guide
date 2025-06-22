# Kafka Broker验证指南

## 1. 环境说明
我们使用Docker Compose搭建了包含以下组件的Kafka环境：
- Zookeeper 3.5.9
- Confluent Kafka 7.2.1 (ARM64架构)

## 2. 验证步骤

### 2.1 创建测试主题
```bash
# 创建名为test的主题，1个分区，1个副本
docker exec -it kafka kafka-topics \
    --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic test
```

### 2.2 查看主题信息
```bash
# 查看主题详细配置信息
docker exec -it kafka kafka-topics \
    --describe \
    --bootstrap-server localhost:9092 \
    --topic test
```

预期输出应包含：
- Topic名称
- PartitionCount
- ReplicationFactor
- 分区信息
- Leader分配
- 副本分配

### 2.3 发送测试消息
```bash
# 启动生产者控制台
docker exec -it kafka kafka-console-producer \
    --bootstrap-server localhost:9092 \
    --topic test
```

在提示符下输入测试消息：
```
Test Message 1
Test Message 2
```
使用 `Ctrl+C` 退出生产者控制台。

### 2.4 消费测试消息
```bash
# 启动消费者控制台
docker exec -it kafka kafka-console-consumer \
    --bootstrap-server localhost:9092 \
    --topic test \
    --from-beginning
```

## 3. 验证成功标准
- 主题创建命令执行成功
- 能够查看到主题详细信息
- 生产者能够成功发送消息
- 消费者能够接收到所有发送的消息

## 4. 注意事项
1. 确保Docker容器正常运行
2. 确保端口9092未被占用
3. 确保有足够的磁盘空间用于日志存储
4. 消费者使用`--from-beginning`参数可以读取主题中的所有历史消息

## 5. 故障排查
如果验证过程中遇到问题，可以：
1. 检查容器日志：
```bash
docker logs kafka
docker logs zookeeper
```

2. 检查数据目录：
```bash
ls -l ./data/kafka/logs    # Kafka数据目录
ls -l ./data/zookeeper/data    # Zookeeper数据目录
```

## 6. 环境配置参考
当前使用的docker-compose.yml配置：
```yaml
version: '3'
services:
  zookeeper:
    image: zookeeper:3.5.9
    platform: linux/arm64/v8  # 指定 ARM64 平台
    container_name: zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOO_MY_ID: 1
      ZOO_PORT: 2181
      ZOO_SERVERS: server.1=zookeeper:2888:3888;2181
    volumes:
      - ./data/zookeeper/data:/data
      - ./data/zookeeper/datalog:/datalog
      - ./data/zookeeper/logs:/logs
    restart: always

  kafka:
    image: confluentinc/cp-kafka:7.2.1
    platform: linux/arm64/v8
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
    volumes:
      - ./data/kafka/logs:/var/lib/kafka/data
    depends_on:
      - zookeeper
    restart: unless-stopped
``` 