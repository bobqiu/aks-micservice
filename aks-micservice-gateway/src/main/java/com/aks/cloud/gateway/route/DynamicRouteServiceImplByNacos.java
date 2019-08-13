package com.aks.cloud.gateway.route;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;
import java.util.concurrent.Executor;

@Slf4j
@Component
public class DynamicRouteServiceImplByNacos {

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String nacosServer;


    @PostConstruct
    public void init() throws NacosException {
        //dynamicRouteByNacosListener("service-dev-aks","aks-g");
    }



    public DynamicRouteServiceImplByNacos() throws NacosException {
        dynamicRouteByNacosListener("service-dev-aks","aks-g");
    }

    /**
     * 监听Nacos Server下发的动态路由配置
     * @param dataId
     * @param group
     */
    public void dynamicRouteByNacosListener (String dataId, String group) throws NacosException {
        try {
            log.info("config server:{},listener:{},{}",nacosServer,dataId,group);
            //ConfigService configService=NacosFactory.createConfigService(nacosServer);
            Properties properties = new Properties();
            properties.put("serverAddr", "127.0.0.1:8848");
            ConfigService configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, group, 5000);
            log.info(content);
            configService.addListener(dataId, group, new Listener()  {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("configInfo:{}",configInfo);
                    RouteDefinition definition= JSON.parseObject(configInfo, RouteDefinition.class);
                    dynamicRouteService.update(definition);
                }
                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            //todo 提醒:异常自行处理此处省略
            throw e;
        }
    }


}
