package com.lun.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 启动ConfigCenterMain3344
 *
 * 浏览器防问 - http://config-3344.com:3344/master/config-dev.yml
 * 配置读取规则
 * /{label}/{name}-{profile}.yml（推荐）
 * label：分支(branch)
 * name：配置文件名称
 * profile：后缀名称,环境(dev/test/prod)
 */
@SpringBootApplication
@EnableConfigServer//启用配置服务
public class ConfigCenterMain3344 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigCenterMain3344.class,args);
    }
}
