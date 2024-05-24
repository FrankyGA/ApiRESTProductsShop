package com.spring.productsshop.mapper;

import java.util.stream.Collectors;

import com.spring.productsshop.dto.OrderListDTO;
import com.spring.productsshop.dto.OrderListItemDTO;
import com.spring.productsshop.model.Order;
import com.spring.productsshop.model.OrderItem;

public class OrderListConvertTo {

    public static OrderListDTO convertToDTO(Order order) {
        OrderListDTO orderDTO = new OrderListDTO();
        orderDTO.setId(order.getId());
        orderDTO.setClientId(order.getClient().getId());
        orderDTO.setTotal(order.getTotal());
        orderDTO.setItems(order.getItems().stream()
            .map(OrderListConvertTo::convertToItemDTO) 
            .collect(Collectors.toList()));
        return orderDTO;
    }

    private static OrderListItemDTO convertToItemDTO(OrderItem orderItem) {
        OrderListItemDTO orderListItemDTO = new OrderListItemDTO();
        orderListItemDTO.setProductId(orderItem.getProduct().getId());
        orderListItemDTO.setName(orderItem.getProduct().getName());
        orderListItemDTO.setBrand(orderItem.getProduct().getBrand());
        orderListItemDTO.setQuantity(orderItem.getQuantity());
        orderListItemDTO.setPrice(orderItem.getPrice());
        return orderListItemDTO;
    }
}




