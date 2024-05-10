package com.spring.productsshop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Products", description = "Products")
@Entity
@Table(name = "products")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_product")
	@Schema(example = "1", description = "ID for product")
	private Long id;
	
	@NotNull(message = "Name is required")
    @Size(min = 3, message = "Name must be at least 3 characters long")
	@Schema(example = "Tomate frito", description = "Name for product")
	private String name;
	
	@Schema(example = "Orlando", description = "Brand of product")
	private String brand;
	
	@NotNull(message = "Price is required")
	@Schema(example = "10.50", description = "Price of product")
	private double price;
	
}
