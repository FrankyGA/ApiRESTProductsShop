package com.spring.productsshop.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
	
	private Long productId;
    private Integer quantity;
    private Double price;

}
