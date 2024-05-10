package com.spring.productsshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.productsshop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByName(String name);
	
	boolean existsByName(String name);

}
