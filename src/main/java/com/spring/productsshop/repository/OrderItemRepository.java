package com.spring.productsshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.productsshop.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
