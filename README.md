# YU-MALL

鱼的电商系统（YU-MALL）是一个面向实战的微服务电商平台，采用 Spring Cloud Alibaba 技术栈，覆盖用户端、管理端、网关、核心交易链路与搜索能力。项目目标是提供可扩展、可演进、可联调的电商系统参考实现。

## 目录

- [项目亮点](#项目亮点)
- [技术栈](#技术栈)
- [系统架构概览](#系统架构概览)
- [模块说明](#模块说明)
- [关键业务链路（搜索）](#关键业务链路搜索)
- [环境准备](#环境准备)
- [快速开始](#快速开始)
- [测试与验证](#测试与验证)
- [开发约定](#开发约定)

## 项目亮点

- 微服务拆分清晰：网关、用户中心、商品、订单、支付、搜索、AI 服务按职责解耦
- 技术栈新：基于 Java 21 + Spring Boot 3.5 + Spring Cloud 2025
- 搜索链路完整：商品变更通过 MQ 同步到 Elasticsearch，支持独立搜索服务
- 前后端分离：`frontend`（用户端）+ `frontend-admin`（管理端）
- 公共能力抽离：`yu-common` 承载通用能力，`yu-api` 承载跨服务契约

## 技术栈

### 后端

- Java 21
- Spring Boot 3.5.13
- Spring Cloud 2025.0.1
- Spring Cloud Alibaba 2025.0.0.0
- MyBatis-Plus 3.5.15
- Spring AI 1.1.4

### 前端

- Vue 3
- TypeScript
- Vite

### 中间件

- MySQL
- Redis
- RabbitMQ
- Nacos
- Elasticsearch（搜索服务依赖）

## 系统架构概览

```text
frontend / frontend-admin
          |
      yu-gateway
          |
  +-------+---------------------------------------------------+
  |       |             |           |          |              |
admin  user-center   product      order       pay           ai
                        |
                     RabbitMQ
                        |
                   search-service
                        |
                 Elasticsearch
```

## 模块说明

### 后端模块（与根 `pom.xml` 一致）

- `admin-service`：后台管理能力
- `user-center-service`：用户认证、用户中心能力
- `product-service`：商品与相关业务
- `search-service`：搜索查询、索引同步消费
- `order-service`：订单业务
- `pay-service`：支付业务
- `ai-service`：AI 能力集成
- `yu-gateway`：统一入口、路由、网关治理
- `yu-common`：公共工具、通用配置、异常模型
- `yu-api`：Feign 接口、DTO/VO、服务间调用契约

### 前端模块

- `frontend`：用户端
- `frontend-admin`：管理端

## 关键业务链路（搜索）

1. 商品数据在 `product-service` 发生新增/更新/上下架
2. 商品服务发送索引同步消息到 RabbitMQ
3. `search-service` 消费消息并更新 Elasticsearch 文档
4. 前端通过搜索接口查询，返回聚合后的搜索结果

## 环境准备

本地联调至少需要以下依赖：

- MySQL
- Redis
- Nacos
- RabbitMQ
- Elasticsearch

常见默认地址（以各服务配置和 Nacos 实际内容为准）：

- Nacos：`192.168.100.128:8848
- Redis：`192.168.100.128:6379`

## 快速开始

### 1. 构建项目

```powershell
# 全量构建（跳过测试）
mvn clean package -DskipTests
```

```powershell
# 修改公共模块后建议先安装，避免下游依赖版本不一致
mvn -pl yu-common,yu-api -am install -DskipTests
```

### 2. 启动核心后端服务（示例顺序）

```powershell
# 1) 用户中心
mvn -pl user-center-service spring-boot:run

# 2) 商品服务
mvn -pl product-service spring-boot:run

# 3) 搜索服务
mvn -pl search-service spring-boot:run

# 4) 网关
mvn -pl yu-gateway spring-boot:run
```

### 3. 启动前端

```powershell
# 用户端
cd frontend
npm install
npm run dev

# 管理端（可选）
cd ../frontend-admin
npm install
npm run dev
```

## 测试与验证

```powershell
# 运行全量测试
mvn clean test

# 运行单模块测试（示例）
mvn -pl search-service test

# 运行指定测试类（示例）
mvn -pl search-service test -Dtest=SearchServiceImplTest
```

## 开发约定

- `yu-api`、`yu-common` 是基础库模块，改动后建议先 `install` 再联调下游服务
- 模块清单以根 `pom.xml` 为准，新增模块时请同步更新 README
- 配置中心与环境参数以 Nacos 和各服务 `application.yaml` 为准
