package com.spring.productsshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.productsshop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	
	List<Order> findByClientId(Long clientId);

}
