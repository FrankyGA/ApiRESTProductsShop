package com.spring.productsshop.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Order productos", description = "Order productos")
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_order")
	@Schema(example = "1", description = "ID for order")
	private Long id;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

	@Column(nullable = false)
	@Schema(example = "10.50", description = "Total price order")
	private double total;

}
