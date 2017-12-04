package com.idealista.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DBConfig
{
    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.driverClassName}")
    private String driverClassName;

    @Value("${db.username}")
    private String userName;

    @Value("${db.password}")
    private String password;

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(dbUrl, userName, password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setSchema("SCRAPER");
        System.out.println("URL: " + dbUrl);
        System.out.println("username: " + userName);
        System.out.println("password: " + password);
        System.out.println("driver: " + driverClassName);
        return dataSource;
    }
}
