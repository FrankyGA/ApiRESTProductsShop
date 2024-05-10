package com.spring.productsshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class SpringProductsShopApiRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProductsShopApiRestApplication.class, args);
	}

}
