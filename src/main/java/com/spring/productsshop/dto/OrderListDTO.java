package com.spring.productsshop.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDTO {
	
	private Long id;
    private Long clientId;
    private List<OrderListItemDTO> items;
    private Double total;

}
