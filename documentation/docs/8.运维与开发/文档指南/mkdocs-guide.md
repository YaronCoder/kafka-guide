# MkDocs 安装和使用指南

本文档介绍如何使用 MkDocs 搭建本地文档站点。

## 1. 环境准备

首先需要创建并激活 Python 虚拟环境：

```bash
# 创建虚拟环境
python3 -m venv venv

# 激活虚拟环境
source venv/bin/activate  # Unix/macOS
# 或
.\venv\Scripts\activate  # Windows
```

## 2. 安装依赖

在虚拟环境中安装必要的包：

```bash
# 安装基本依赖
pip install -r requirements.txt

# 安装图像处理相关依赖
pip install "mkdocs-material[imaging]"
```

requirements.txt 文件内容：
```txt
mkdocs-material>=9.5.0
pymdown-extensions>=10.5
mkdocs-rss-plugin>=1.12.1
mkdocs-blog-plugin>=0.25
mkdocs-git-revision-date-localized-plugin>=1.2.4
mkdocs-social-plugin>=0.1.0
```

## 3. 配置文件说明

MkDocs 的主要配置文件是 `mkdocs.yml`，包含以下主要配置：

- 站点信息（名称、描述等）
- 主题设置（Material 主题）
- 导航结构
- 插件配置
- Markdown 扩展

## 4. 本地预览

### 4.1 普通预览（仅本机访问）

```bash
mkdocs serve
```

然后在浏览器访问 `http://127.0.0.1:8000`

### 4.2 局域网访问（手机可访问）

```bash
# 查看本机 IP
ifconfig | grep "inet " | grep -v 127.0.0.1

# 启动服务器（允许局域网访问）
mkdocs serve -a 0.0.0.0:8000
```

然后在手机浏览器访问 `http://<你的电脑IP>:8000`

注意事项：
- 确保手机和电脑在同一个局域网（WiFi）下
- 可能需要检查电脑防火墙设置
- 仅限局域网内访问

## 5. 部署到 GitHub Pages

1. 在 GitHub 创建仓库
2. 配置 GitHub Actions（使用 `.github/workflows/deploy.yml`）
3. 推送代码到 GitHub：
```bash
git add .
git commit -m "添加文档"
git push
```

## 6. 目录结构

```
docs/
├── index.md          # 首页
├── blog/             # 博客文章
│   ├── index.md     # 博客首页
│   └── posts/       # 文章目录
└── ...              # 其他文档

mkdocs.yml           # 配置文件
requirements.txt     # 依赖文件
```

## 7. 常见问题

1. 如果遇到 "No module named 'xxx'" 错误，检查是否已安装所有依赖
2. 如果手机无法访问，检查：
   - 是否使用了 `0.0.0.0:8000` 地址启动服务器
   - 手机和电脑是否在同一网络
   - 防火墙设置
3. 如果页面样式异常，可能是主题或插件配置问题，检查 `mkdocs.yml` 配置

## 8. 更多资源

- [MkDocs 官方文档](https://www.mkdocs.org/)
- [Material for MkDocs](https://squidfunk.github.io/mkdocs-material/)
- [PyMdown Extensions](https://facelessuser.github.io/pymdown-extensions/) 