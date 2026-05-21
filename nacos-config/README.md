# YU-MALL Nacos 配置说明

## 基本信息

| 配置项 | 值 |
|--------|-----|
| Nacos 地址 | 192.168.100.128:8848 |
| 命名空间 (Namespace) | yu-mall |
| Group | DEFAULT_GROUP |
| 用户名/密码 | nacos / nacos |

## 配置文件清单

### 共享配置（被多个服务引用）

| Data ID | 文件名 | 引用的服务 | 说明 |
|---------|--------|-----------|------|
| shared-jdbc.yaml | shared-jdbc.yaml | order, pay, product, user-center, admin | MySQL + Druid + MyBatis-Plus |
| shared-log.yaml | shared-log.yaml | 所有服务 | 日志级别与滚动策略 |
| shared-seata.yaml | shared-seata.yaml | order, pay, product, user-center, admin | Seata AT 模式 + Nacos 注册 |
| shared-sentinel.yaml | shared-sentinel.yaml | order, pay, product, user-center | Sentinel 限流 + Nacos 数据源 |
| shared-feign.yaml | shared-feign.yaml | order, pay, product, user-center, search, ai, admin | Feign/OkHttp 超时配置 |
| shared-mq.yaml | shared-mq.yaml | order, pay, product, user-center, search | RabbitMQ 连接与重试 |

### 服务专属配置

| Data ID | 文件名 | 服务名 | 端口 | 说明 |
|---------|--------|--------|------|------|
| order-service.yaml | order-service.yaml | yu-mall-order-service | 8083 | 数据库 mall-order, Redis, MQ 队列, ShedLock |
| pay-service.yaml | pay-service.yaml | yu-mall-pay-service | 8086 | 数据库 mall-pay, MQ 支付队列 |
| product-service.yaml | product-service.yaml | yu-mall-product-service | 8082 | 数据库 mall-item, Redis, MQ 同步队列, 文件上传 |
| user-center-service.yaml | user-center-service.yaml | yu-mall-user-center-service | 8081 | 数据库 mall-user, Redis, JWT, OSS 文件存储 |
| search-service.yaml | search-service.yaml | yu-mall-search-service | 8087 | ES 连接, MQ 同步队列, 搜索索引配置 |
| ai-service.yaml | ai-service.yaml | yu-mall-ai-service | 8092 | Spring AI / OpenAI 配置, 自定义 AI 属性 |
| admin-service.yaml | admin-service.yaml | yu-mall-admin-service | 8084 | 数据库 mall-sys, Redis, JWT, OSS, Seata 禁用 |
| gateway-service.yaml | gateway-service.yaml | yu-mall-gateway-service | 8080 | JWT 验证, 认证路径配置 |

### 动态路由配置

| Data ID | 文件名 | 格式 | 说明 |
|---------|--------|------|------|
| gateway-routes.json | gateway-routes.json | JSON | 网关动态路由，支持 Nacos 监听热更新 |

## 各服务 Nacos Import 对应关系

### user-center-service / order-service / pay-service / product-service
```yaml
spring:
  config:
    import:
      - optional:nacos:shared-jdbc.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:shared-log.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:shared-seata.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:shared-sentinel.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:shared-feign.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:shared-mq.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:<service-name>.yaml?group=DEFAULT_GROUP&refreshEnabled=true
```

### search-service
```yaml
spring:
  config:
    import:
      - optional:nacos:shared-log.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:shared-feign.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:shared-mq.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:search-service.yaml?group=DEFAULT_GROUP&refreshEnabled=true
```

### ai-service
```yaml
spring:
  config:
    import:
      - optional:nacos:shared-log.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:ai-service.yaml?group=DEFAULT_GROUP&refreshEnabled=true
```

### gateway
```yaml
spring:
  config:
    import:
      - optional:nacos:shared-log.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:gateway-routes.json?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:gateway-service.yaml?group=DEFAULT_GROUP&refreshEnabled=true
```

### admin-service（当前 application.yaml 未配置 Nacos import，需要手动添加）
```yaml
spring:
  config:
    import:
      - optional:nacos:shared-jdbc.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:shared-log.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:shared-seata.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:shared-feign.yaml?group=DEFAULT_GROUP&refreshEnabled=true
      - optional:nacos:admin-service.yaml?group=DEFAULT_GROUP&refreshEnabled=true
```

## 占位符说明

配置中使用 `${ENV_VAR:default}` 格式的占位符，可通过环境变量或 JVM 参数覆盖：

| 占位符 | 默认值 | 说明 |
|--------|--------|------|
| MYSQL_HOST | 192.168.100.128 | MySQL 主机 |
| MYSQL_PORT | 3306 | MySQL 端口 |
| MYSQL_DATABASE | mall | 默认数据库名（各服务专属配置中覆盖为具体库名） |
| MYSQL_USERNAME | root | MySQL 用户名 |
| MYSQL_PASSWORD | root | MySQL 密码 |
| REDIS_HOST | 192.168.100.128 | Redis 主机 |
| REDIS_PORT | 6379 | Redis 端口 |
| REDIS_PASSWORD | (空) | Redis 密码 |
| RABBITMQ_HOST | 192.168.100.128 | RabbitMQ 主机 |
| RABBITMQ_PORT | 5672 | RabbitMQ 端口 |
| RABBITMQ_VHOST | /yu-mall | RabbitMQ 虚拟主机 |
| RABBITMQ_USERNAME | guest | RabbitMQ 用户名 |
| RABBITMQ_PASSWORD | guest | RabbitMQ 密码 |
| NACOS_HOST | 192.168.100.128 | Nacos 主机 |
| NACOS_PORT | 8848 | Nacos 端口 |
| NACOS_USERNAME | nacos | Nacos 用户名 |
| NACOS_PASSWORD | nacos | Nacos 密码 |
| SENTINEL_DASHBOARD_HOST | 192.168.100.128 | Sentinel Dashboard 主机 |
| SENTINEL_DASHBOARD_PORT | 8080 | Sentinel Dashboard 端口 |
| SEARCH_ELASTICSEARCH_URIS | http://192.168.100.128:9200 | Elasticsearch 地址 |
| AI_OPENAI_BASE_URL | https://dashscope.aliyuncs.com/compatible-mode | AI API Base URL |
| AI_OPENAI_API_KEY | sk-local-placeholder | AI API Key |
| AI_DEFAULT_MODEL | qwen-plus | AI 默认模型 |
| AI_SYSTEM_PROMPT | (见配置) | AI 系统提示词 |
| JWT_KEYSTORE_PASSWORD | yumall | JWT 密钥库密码 |
| JWT_KEY_ALIAS | yumall | JWT 密钥别名 |
| ALIYUN_OSS_ACCESS_KEY | your-access-key | 阿里云 OSS AccessKey |
| ALIYUN_OSS_ACCESS_SECRET | your-access-secret | 阿里云 OSS AccessSecret |
| ALIYUN_OSS_BUCKET | yu-mall | 阿里云 OSS Bucket |
| ALIYUN_OSS_ENDPOINT | oss-cn-hangzhou.aliyuncs.com | 阿里云 OSS Endpoint |
| ALIYUN_OSS_DOMAIN | (空) | 阿里云 OSS 自定义域名 |
| DRUID_USERNAME | admin | Druid 监控用户名 |
| DRUID_PASSWORD | admin | Druid 监控密码 |

## 数据库对应关系

| 服务 | 数据库名 | SQL 目录 |
|------|---------|---------|
| user-center-service | mall-user | sql/mall-user/ |
| product-service | mall-item | sql/mall-item/ |
| order-service | mall-order | sql/mall-order/ |
| pay-service | mall-pay | sql/mall-pay/ |
| admin-service | mall-sys | sql/mall-sys/ |

## 自定义配置属性来源

| 前缀 | 属性类 | 服务 | 属性 |
|------|--------|------|------|
| yu.jwt | JwtProperties | gateway, admin, user-center | location, password, alias, tokenTTL |
| yu.auth | AuthProperties | gateway | includePaths, excludePaths |
| yu.ai | YuAiProperties | ai-service | defaultModel, systemPrompt, defaultTemperature |
| yu.search | SearchProperties | search-service | indexName, rebuildPageSize |

## 导入步骤

1. 登录 Nacos 控制台 (http://192.168.100.128:8848/nacos)
2. 创建命名空间 `yu-mall`（如果不存在）
3. 切换到 `yu-mall` 命名空间
4. 逐个创建配置，Data ID 和 Group 按上表填写，格式选 YAML（gateway-routes.json 选 JSON）
5. 将本目录下对应文件的内容粘贴到配置内容中
6. 发布配置
7. 启动各服务验证
