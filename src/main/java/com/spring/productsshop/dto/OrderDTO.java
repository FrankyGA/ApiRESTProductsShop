package com.spring.productsshop.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

	private Long id;
	private Long clientId;
    private List<OrderItemDTO> items;
    private Double total;
    
	
}
