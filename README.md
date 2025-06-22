# Kafka 学习指南项目

这是一个综合性的 Kafka 学习项目，包含示例代码和详细文档。

## 项目结构

```
kafka-guide/
├── src/                    # Java 源代码目录
│   └── main/
│       ├── java/          # Java 代码
│       └── resources/     # 配置文件
│
├── documentation/          # 文档项目目录
│   ├── docs/              # MkDocs 文档源文件
│   ├── mkdocs.yml        # MkDocs 配置
│   ├── requirements.txt   # Python 依赖
│   └── venv/             # Python 虚拟环境（不提交到git）
│
├── docker/                # Docker 相关配置
│   ├── config/           # Kafka 配置文件
│   └── docker-compose.yml
│
├── pom.xml               # Maven 项目配置
└── README.md             # 项目说明文档
```

## 开发指南

### Java 项目

这是一个 Maven 项目，包含 Kafka 相关的示例代码和最佳实践：

```bash
# 编译项目
mvn clean install

# 运行测试
mvn test
```

### 文档项目

文档使用 MkDocs 构建，位于 `documentation` 目录：

```bash
# 进入文档目录
cd documentation

# 安装文档依赖
python3 -m venv venv
source venv/bin/activate  # Unix/macOS
pip install -r requirements.txt

# 本地预览文档
mkdocs serve

# 构建文档
mkdocs build
```

详细的文档开发指南请参考：[MkDocs 使用指南](https://your-github-username.github.io/kafka-guide/mkdocs-guide/)

## Docker 环境

项目包含完整的 Docker 配置，可以快速启动 Kafka 集群：

```bash
# 启动 Kafka 集群
cd docker
docker-compose up -d
```

## 文档站点

- 在线文档：https://your-github-username.github.io/kafka-guide/
- 本地文档：http://localhost:8000 (运行 `mkdocs serve` 后访问)

## 贡献指南

1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的改动 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建一个 Pull Request
