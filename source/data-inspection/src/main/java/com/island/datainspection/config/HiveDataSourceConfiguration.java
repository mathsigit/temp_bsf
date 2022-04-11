package com.island.datainspection.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:persistence-db-config.yaml"})
@EncryptablePropertySource("persistence-db-config.yaml")
public class HiveDataSourceConfiguration {

    @Value("${spring.hive-datasource.driver-class-name}")
    private String DRIVER_CLASS_NAME;

    @Value("${spring.hive-datasource.jdbc-url}")
    private String JDBC_URL;

    @Bean(name = "HiveDataSource")
    public DataSource getDBDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(JDBC_URL);
        return dataSource;
    }
}
