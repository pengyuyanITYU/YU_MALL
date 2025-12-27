package com.yu.gateway.routes;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;


@RequiredArgsConstructor
@Component
@Slf4j
public class DynamicRouteLoader {

    private final String dataId =  "gateway-routes.json";
    private final String group = "DEFAULT_GROUP";
    //RouteDefinitionWriter主要来对 RouteDefinition(描述路由规则的核心实体类)进行增删改
    private final RouteDefinitionWriter routeDefinitionWriter;
    //Spring 应用与Nacos配置中心交互的核心管理类
    private final NacosConfigManager nacosConfigManager;
    private final Set<String> routeIds = new HashSet<>();

    //该类初始化(完成构造函数和di依赖的加载)之后自动执行该方法
    @PostConstruct
    public void initRouteConfigListener() throws NacosException {
        nacosConfigManager.getConfigService().getConfigAndSignListener(dataId, group, 5000,new Listener(){

            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String config) {
                log.debug("监听到路由配置变更，{}", config);
                updateConfig(config);
            }
        });
    }
    //这是 Nacos 配置中心中 配置变更监听器的回调方法，
    // 当监听的配置发生变化时，
    // Nacos 会自动调用该方法，并将最新的配置内容（字符串形式）通过参数config传入。
    private void updateConfig(String config){
        // 配置更新时，解析新的路由规则并更新网关
        List<RouteDefinition> list = JSONUtil.toList(config, RouteDefinition.class);
        for (String routeId : routeIds) {
            routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
        }
        routeIds.clear();
        // 2.2.判断是否有新的路由要更新
        if (CollUtil.isEmpty(list)) {
            // 无新路由配置，直接结束
            return;
        }
        for (RouteDefinition routeDefinition : list) {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            /*
             Mono.just(routeId) //把路由ID包装成一个Mono对象
             因为Spring Cloud Gateway 基于响应式编程（Reactor），
             其 API 方法的参数和返回值通常是 Mono（单值响应式类型）。

             .subscribe()触发响应式操作的执行。
             */
            routeIds.add(routeDefinition.getId());
        }
    }

}
