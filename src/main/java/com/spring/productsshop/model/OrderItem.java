package com.spring.productsshop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "OrderItem productos", description = "OrderItem productos")
@Entity
@Table(name = "order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(example = "1", description = "ID for order")
	@Column(name = "id_order_items")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_product", nullable = false)
	@Schema(example = "1", description = "ID product")
	private Product product;

	@Column(nullable = false)
	@Schema(example = "1", description = "Quantity of product")
	private Integer quantity;

	@Column(nullable = false)
	@Schema(example = "1", description = "Price")
	private Double price;

	@ManyToOne
	@JoinColumn(name = "id_order", nullable = false)
	@Schema(example = "1", description = "ID order")
	private Order order;
}
