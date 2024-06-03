package com.spring.productsshop.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.spring.productsshop.dto.OrderDTO;
import com.spring.productsshop.dto.OrderItemDTO;
import com.spring.productsshop.exception.ResourceNotFoundException;
import com.spring.productsshop.model.Order;
import com.spring.productsshop.model.OrderItem;
import com.spring.productsshop.model.Product;

public class OrderConvertTo {

    public static OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setClientId(order.getClient().getId());
        orderDTO.setTotal(order.getTotal());
        orderDTO.setItems(order.getItems().stream()
            .map(OrderConvertTo::convertToItemDTO)
            .collect(Collectors.toList()));
        return orderDTO;
    }

    public static Order convertToEntity(OrderDTO orderDTO, List<OrderItem> items) {
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setTotal(orderDTO.getTotal());
        order.setItems(items);
        return order;
    }

    private static OrderItemDTO convertToItemDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(orderItem.getProduct().getId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setPrice(orderItem.getPrice());
        return orderItemDTO;
    }

    public static OrderItem convertToItemEntity(OrderItemDTO orderItemDTO, Order order, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setPrice(orderItemDTO.getPrice());
        orderItem.setOrder(order);  // Assign the order to the order item
        return orderItem;
    }

    public static List<OrderItemDTO> convertToItemDTOList(List<OrderItem> items) {
        return items.stream()
            .map(OrderConvertTo::convertToItemDTO)
            .collect(Collectors.toList());
    }

    public static List<OrderItem> convertToItemEntityList(List<OrderItemDTO> itemDTOs, Order order, List<Product> products) {
        return itemDTOs.stream()
            .map(dto -> {
                Product product = products.stream()
                    .filter(p -> p.getId().equals(dto.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + dto.getProductId()));
                return convertToItemEntity(dto, order, product);
            })
            .collect(Collectors.toList());
    }
}


