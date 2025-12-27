package com.yu.order.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.util.TimeZone;

@Configuration
@EnableScheduling // 开启 Spring 自带定时任务
// defaultLockAtMostFor: 默认最大锁定时间，防止任务挂了死锁 (例如 10分钟)
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
public class ScheduledTaskConfig {

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        // 使用 Spring 容器中已有的 DataSource (MyBatis-Plus 会自动配置好)
        return new JdbcTemplateLockProvider(
            JdbcTemplateLockProvider.Configuration.builder()
                .withJdbcTemplate(new JdbcTemplate(dataSource))
                .usingDbTime() // 关键：使用数据库时间，防止不同服务器系统时间不一致导致锁失效
                .build()
        );
    }
}