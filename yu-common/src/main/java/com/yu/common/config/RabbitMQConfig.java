package com.yu.common.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 自定义配置
 * * 使用 @ConditionalOnClass 确保只有在项目中引入了 RabbitTemplate 相关依赖时才加载此配置
 */
@Configuration
@ConditionalOnClass(RabbitTemplate.class)
public class RabbitMQConfig {

    /**
     * 配置消息转换器：使用 JSON 序列化代替默认的 JDK 序列化
     * 1. 跨语言支持更好
     * 2. 可读性更强
     * 3. 性能更高
     */
    @Bean
    @ConditionalOnMissingBean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter jjmc = new Jackson2JsonMessageConverter();

        // 设置为发送消息时自动创建消息 ID，便于追踪和幂等性处理
        jjmc.setCreateMessageIds(true);

        return jjmc;
    }
}