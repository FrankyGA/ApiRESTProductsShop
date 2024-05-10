package com.spring.productsshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	
	
	private Long id;
	private String name;
	private String brand;
	private double price;
	
}
