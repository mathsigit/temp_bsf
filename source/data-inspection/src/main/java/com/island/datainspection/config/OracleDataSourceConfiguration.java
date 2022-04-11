package com.island.datainspection.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration(value = "OracleDataSourceConfiguration")
@PropertySource({"classpath:persistence-db-config.yaml"})
@EncryptablePropertySource("persistence-db-config.yaml")
@Data
public class OracleDataSourceConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String DRIVER_CLASS_NAME;

    @Value("${spring.datasource.jdbc-url}")
    private String JDBC_URL;

    @Value("${spring.datasource.username}")
    private String USERNAME;

    @Value("${spring.datasource.password}")
    private String PASSWORD;

    @Bean(name = "OracleDataSource")
    public DataSource getDBDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(JDBC_URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }
}
