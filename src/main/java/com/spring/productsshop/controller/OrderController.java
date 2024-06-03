package com.spring.productsshop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.productsshop.dto.OrderDTO;
import com.spring.productsshop.dto.OrderItemDTO;
import com.spring.productsshop.dto.OrderListDTO;
import com.spring.productsshop.exception.ErrorDetails;
import com.spring.productsshop.exception.ResourceNotFoundException;
import com.spring.productsshop.mapper.OrderConvertTo;
import com.spring.productsshop.mapper.OrderListConvertTo;
import com.spring.productsshop.model.Client;
import com.spring.productsshop.model.Order;
import com.spring.productsshop.model.OrderItem;
import com.spring.productsshop.model.Product;
import com.spring.productsshop.repository.ClientRepository;
import com.spring.productsshop.repository.OrderItemRepository;
import com.spring.productsshop.repository.OrderRepository;
import com.spring.productsshop.repository.ProductRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Order", description = "Order controller with CRUD Operations")
public class OrderController {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;
	private final ClientRepository clientRepository;

	@Autowired
	public OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
			ProductRepository productRepository, ClientRepository clientRepository) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
		this.clientRepository = clientRepository;
	}
	
	// --------------------- Método para consultar todos los pedidos ---------------------//
		@Operation(summary = "Get all orders", description = "Get a list of all orders") // Documentación de Swagger/OpenAPI
		@ApiResponses(value = {
				@ApiResponse(responseCode = "200", description = "Orders found, retrieved orders", content = {
						@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class))) }),
				@ApiResponse(responseCode = "404", description = "Orders not found", content = @Content(schema = @Schema(implementation = ErrorDetails.class))) })
		@GetMapping("/orders/all") // Mapeo de la solicitud GET para obtener todos los pedidos
		public ResponseEntity<List<OrderListDTO>> getAllOrders() {
			List<Order> orders = orderRepository.findAll(); // Consulta para obtener todos los pedidos
			if (orders.isEmpty()) { // Verifica si la lista de pedidos está vacía
				throw new ResourceNotFoundException("No orders found"); // Lanza una excepción si no se encuentran pedidos
			}
			List<OrderListDTO> orderListDTOs = orders.stream().map(OrderListConvertTo::convertToDTO) // Convierte las entidades de pedidos a DTOs
					.collect(Collectors.toList());
			return ResponseEntity.ok(orderListDTOs); // Devuelve la lista de pedidos en el cuerpo de la respuesta
		}

	// --------------------- Método para consultar pedidos por cliente ---------------------//
	@Operation(summary = "Get orders by client", description = "Get orders by client")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Orders found, retrieved orders", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderListDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Orders not found", content = @Content(schema = @Schema(implementation = ErrorDetails.class))) })
	@GetMapping("/ordersClient")
	public ResponseEntity<List<OrderListDTO>> getOrdersByClient(@RequestParam Long clientId) {
		List<Order> orders = orderRepository.findByClientId(clientId); // Consultar pedidos por ID de cliente
		if (orders.isEmpty()) {
			throw new ResourceNotFoundException("No orders found for client with ID: " + clientId); 
		}
		List<OrderListDTO> orderListDTOs = orders.stream().map(OrderListConvertTo::convertToDTO) // Convertir entidades de pedido a DTO
				.collect(Collectors.toList());
		return ResponseEntity.ok(orderListDTOs); // Devolver lista de pedidos
	}

	// --------------------- Método para consultar pedido por ID ---------------------//
	@Operation(summary = "Get orders by id", description = "Get orders by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Orders found, retrieved orders", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Orders not found", content = @Content(schema = @Schema(implementation = ErrorDetails.class))) })
	@GetMapping("/orders/{id}")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
		Order order = orderRepository.findById(id) // Consultar pedido por ID
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id)); 
		return ResponseEntity.ok(OrderConvertTo.convertToDTO(order)); // Devolver DTO del pedido encontrado
	}

	//--------------------- Método para crear un nuevo pedido ---------------------//
	@Operation(summary = "Create a new order", description = "Create a new order")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Order created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid data provided", content = @Content(schema = @Schema(implementation = ErrorDetails.class))) })
	@PostMapping("/orders")
	public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
		try {
			// Buscar el cliente por su ID
			Client client = clientRepository.findById(orderDTO.getClientId()).orElseThrow(
					() -> new ResourceNotFoundException("Client not found with id: " + orderDTO.getClientId()));

			// Buscar los productos por sus IDs
			List<Product> products = productRepository.findAllById(
					orderDTO.getItems().stream().map(OrderItemDTO::getProductId).collect(Collectors.toList()));

			// Convertir la orden DTO en una entidad Order
			Order order = new Order();
			order.setClient(client); // Vincular el cliente al pedido
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

	//--------------------- Método para eliminar un pedido por su ID ---------------------//
	@Operation(summary = "Delete order by ID", description = "Delete order by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Order deleted"),
			@ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = ErrorDetails.class))) })
	@DeleteMapping("/orders/{id}")
	public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
		orderRepository.delete(order);
		return ResponseEntity.noContent().build();
	}
}
