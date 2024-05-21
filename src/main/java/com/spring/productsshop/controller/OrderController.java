package com.spring.productsshop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.productsshop.dto.ClientDTO;
import com.spring.productsshop.dto.OrderDTO;
import com.spring.productsshop.dto.OrderItemDTO;
import com.spring.productsshop.dto.ProductDTO;
import com.spring.productsshop.exception.ResourceNotFoundException;
import com.spring.productsshop.mapper.ClientConvertTo;
import com.spring.productsshop.mapper.OrderConvertTo;
import com.spring.productsshop.mapper.ProductConvertTo;
import com.spring.productsshop.model.Client;
import com.spring.productsshop.model.Order;
import com.spring.productsshop.model.OrderItem;
import com.spring.productsshop.model.Product;
import com.spring.productsshop.repository.ClientRepository;
import com.spring.productsshop.repository.OrderItemRepository;
import com.spring.productsshop.repository.OrderRepository;
import com.spring.productsshop.repository.ProductRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Order", description = "Order controller with CRUD Operations")
public class OrderController {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;

	@Autowired
	public OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
			ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
	}

	@PostMapping("/orders")
	public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
		try {
			// Buscar los productos por sus IDs
			List<Product> products = productRepository.findAllById(
					orderDTO.getItems().stream().map(OrderItemDTO::getProductId).collect(Collectors.toList()));

			// Convertir la orden DTO en una entidad Order
			Order order = new Order();
			order.setItems(new ArrayList<>());

			// Guardar el pedido
			Order savedOrder = orderRepository.save(order);
			List<OrderItem> orderItems = orderDTO.getItems().stream().map(dto -> {
				Product product = products.stream().filter(p -> p.getId().equals(dto.getProductId())).findFirst()
						.orElseThrow(() -> new ResourceNotFoundException(
								"Product not found with id: " + dto.getProductId()));
				return new OrderItem(null, product, dto.getQuantity(), dto.getPrice(), savedOrder);
			}).collect(Collectors.toList());

			orderItemRepository.saveAll(orderItems);

			// Calcular el total del pedido
			double total = orderItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
			savedOrder.setTotal(total);
			orderRepository.save(savedOrder);

			// Devolver el pedido guardado
			return ResponseEntity.status(HttpStatus.CREATED).body(OrderConvertTo.convertToDTO(savedOrder));
		} catch (Exception ex) {
			// Manejar cualquier error y devolver un mensaje de error apropiado
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/orders/{id}")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
		return ResponseEntity.ok(OrderConvertTo.convertToDTO(order));
	}
}
