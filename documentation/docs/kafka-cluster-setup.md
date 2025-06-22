# Kafka集群部署文档

## 1. 集群架构
当前集群配置包含以下组件：
- 3个Kafka broker节点
- 1个Zookeeper节点
- 所有组件都运行在Docker容器中

### 1.1 节点分布
| 服务名称 | 容器名称 | 端口 | 数据目录 |
|---------|---------|------|---------|
| Zookeeper | zookeeper | 2181 | ./data/zookeeper/ |
| Kafka Broker 1 | kafka1 | 9092 | ./data/kafka1/logs |
| Kafka Broker 2 | kafka2 | 9093 | ./data/kafka2/logs |
| Kafka Broker 3 | kafka3 | 9094 | ./data/kafka3/logs |

## 2. 高可用配置
集群具有以下高可用特性：
- 复制因子(Replication Factor) = 3
- 最小同步副本(Min ISR) = 2
- 事务日志复制因子 = 3
- 自动重启策略：unless-stopped

## 3. 部署步骤

### 3.1 准备工作
创建必要的数据目录：
```bash
mkdir -p ./data/kafka1/logs
mkdir -p ./data/kafka2/logs
mkdir -p ./data/kafka3/logs
mkdir -p ./data/zookeeper/data
mkdir -p ./data/zookeeper/datalog
mkdir -p ./data/zookeeper/logs
```

### 3.2 启动集群
```bash
docker-compose up -d
```

### 3.3 验证集群状态
1. 检查容器状态：
```bash
docker-compose ps
```

2. 查看broker列表：
```bash
docker exec -it kafka1 kafka-topics --bootstrap-server localhost:9092 --describe --topic __consumer_offsets
```

## 4. 集群使用指南

### 4.1 创建主题
```bash
docker exec -it kafka1 kafka-topics --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 3 \
    --partitions 3 \
    --topic my-topic
```

### 4.2 查看主题详情
```bash
docker exec -it kafka1 kafka-topics --describe \
    --bootstrap-server localhost:9092 \
    --topic my-topic
```

### 4.3 生产消息
```bash
docker exec -it kafka1 kafka-console-producer \
    --bootstrap-server localhost:9092 \
    --topic my-topic
```

### 4.4 消费消息
```bash
docker exec -it kafka1 kafka-console-consumer \
    --bootstrap-server localhost:9092 \
    --topic my-topic \
    --from-beginning
```

## 5. 配置说明

### 5.1 Zookeeper配置
```yaml
environment:
  ZOO_MY_ID: 1
  ZOO_PORT: 2181
  ZOO_SERVERS: server.1=zookeeper:2888:3888;2181
```

### 5.2 Kafka Broker配置
每个broker的关键配置：
```yaml
environment:
  KAFKA_BROKER_ID: [1-3]  # 每个broker唯一
  KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
  KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:[9092-9094]
  KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:[9092-9094]
  KAFKA_AUTO_CREATE_TOPICS_ENABLE: false
  KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
  KAFKA_MIN_INSYNC_REPLICAS: 2
```

## 6. 监控和维护

### 6.1 查看日志
```bash
# Zookeeper日志
docker logs zookeeper

# Kafka日志
docker logs kafka1
docker logs kafka2
docker logs kafka3
```

### 6.2 检查数据目录
```bash
# 查看数据目录大小
du -sh ./data/kafka*/logs
du -sh ./data/zookeeper/*
```

## 7. 注意事项
1. 集群禁用了自动创建主题功能
2. 主题创建时建议使用3个分区和3个副本
3. 生产环境建议配置JVM参数和日志清理策略
4. 需要确保足够的磁盘空间

## 8. 故障处理
1. 如果单个broker宕机：
   - 集群仍然可以正常工作
   - 自动重启策略会尝试恢复该broker
   - 其他broker会接管领导者角色

2. 如果出现连接问题：
   - 检查网络连接
   - 验证端口是否正确暴露
   - 确认advertised.listeners配置是否正确

3. 如果出现数据不一致：
   - 检查min.insync.replicas配置
   - 验证复制因子设置
   - 检查磁盘空间使用情况 