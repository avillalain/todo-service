package com.demo.todo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MariaDBContainer;

@TestConfiguration(proxyBeanMethods = false)
public class DatasourceConfiguration {

    @Bean
    @ServiceConnection("datasource")
    public MariaDBContainer mariadb() {
        return new MariaDBContainer("mariadb:latest");
    }
}
