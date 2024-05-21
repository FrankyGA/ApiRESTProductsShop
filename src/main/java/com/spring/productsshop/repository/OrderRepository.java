package com.spring.productsshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.productsshop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
