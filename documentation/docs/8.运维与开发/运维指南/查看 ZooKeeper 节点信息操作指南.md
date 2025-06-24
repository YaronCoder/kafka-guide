# 查看 ZooKeeper 节点信息操作指南

## 1. 进入容器
```bash
docker exec -it zookeeper /bin/bash
```

## 2. 启动客户端
```bash
zkCli.sh
```

## 3. 查看节点信息
查看根节点：
```bash
ls /
```

查看已注册的 broker：
```bash
ls /brokers/ids
```

查看特定 broker 详情：
```bash
get /brokers/ids/1
```

递归查看所有 broker 信息：
```bash
ls -R /brokers
```

## 4. 退出操作
退出 ZK 客户端：
```bash
quit
```

退出容器：
```bash
exit
```
