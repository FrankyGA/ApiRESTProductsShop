package com.spring.productsshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); // Controlador de MySQL
        dataSource.setUrl("jdbc:mysql://localhost:3306/productsshop"); // URL de la base de datos
        dataSource.setUsername("userProducts"); // Nombre de usuario de la base de datos
        dataSource.setPassword("userProducts"); // Contraseña de la base de datos
        return dataSource;
    }
}
