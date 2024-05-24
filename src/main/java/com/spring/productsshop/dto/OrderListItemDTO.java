package com.spring.productsshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListItemDTO {
    private Long productId;
    private String productName;
    private String brand;
    private Integer quantity;
    private Double price;
}

