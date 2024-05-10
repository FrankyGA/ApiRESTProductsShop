package com.spring.productsshop.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	/**
	 * Configuraci�n m�s b�sica. Por defecto se permite
	 * - Todos los or�gnenes
	 * - M�todos GET, HEAD, POST
	 * 
	 */
	
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**");
//			}
//			
//		};
//	}
	
	/**
	 * Configuraci�n m�s ajustada.
	 */
	/*
	 * @Bean public WebMvcConfigurer corsConfigurer() { return new
	 * WebMvcConfigurer() {
	 * 
	 * @Override public void addCorsMappings(CorsRegistry registry) {
	 * registry.addMapping("/producto/**") .allowedOrigins("http://localhost:9001")
	 * .allowedMethods("GET", "POST", "PUT", "DELETE") .maxAge(3600); }
	 * 
	 * }; }
	 */
}
