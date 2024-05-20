package com.spring.productsshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.productsshop.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
	
	Optional<Client> findByName(String name);

}
