package com.spring.productsshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.productsshop.dto.OrderDTO;
import com.spring.productsshop.dto.OrderItemDTO;
import com.spring.productsshop.dto.OrderListDTO;
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

@Service // Indica que esta clase es un servicio gestionado por Spring.
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    @Autowired // Inyección de dependencias de los repositorios.
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        ProductRepository productRepository, ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Obtiene todos los pedidos de la base de datos.
     *
     * @return Lista de todos los pedidos.
     */
    // Método para obtener todos los pedidos.
    public List<OrderListDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll(); // Recupera todos los pedidos desde la base de datos.
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found"); // Lanza una excepción si no se encuentran pedidos.
        }
        return orders.stream().map(OrderListConvertTo::convertToDTO) // Convierte la lista de entidades Order a OrderListDTO.
                .collect(Collectors.toList()); // Retorna la lista de DTOs.
    }

    /**
     * Obtiene los pedidos de un cliente por su ID.
     *
     * @param id ID del cliente a buscar.
     * @return Devuelve los pedidos por cliente
     */
    // Método para obtener los pedidos por ID de cliente.
    public List<OrderListDTO> getOrdersByClient(Long clientId) {
        List<Order> orders = orderRepository.findByClientId(clientId); // Recupera los pedidos asociados al cliente.
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for client with ID: " + clientId); // Lanza una excepción si no se encuentran pedidos.
        }
        return orders.stream().map(OrderListConvertTo::convertToDTO) // Convierte la lista de entidades Order a OrderListDTO.
                .collect(Collectors.toList()); // Retorna la lista de DTOs.
    }
    
    /**
     * Obtiene pedido por su ID.
     *
     * @param id ID del pedido a buscar.
     * @return Devuelve el pedido
     */
    // Método para obtener un pedido por su ID.
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id) // Recupera el pedido por su ID.
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id)); // Lanza una excepción si no se encuentra el pedido.
        return OrderConvertTo.convertToDTO(order); // Convierte la entidad Order a OrderDTO.
    }

    /**
     * Crea un nuevo pedido en la base de datos.
     *
     * @param order Order a crear.
     * @return El pedido creado.
     */
    // Método para crear un nuevo pedido.
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Buscar el cliente por su ID.
        Client client = clientRepository.findById(orderDTO.getClientId()).orElseThrow(
                () -> new ResourceNotFoundException("Client not found with id: " + orderDTO.getClientId()));

        // Buscar los productos por sus IDs.
        List<Product> products = productRepository.findAllById(
                orderDTO.getItems().stream().map(OrderItemDTO::getProductId).collect(Collectors.toList()));

        // Crear una nueva entidad Order y asignarle el cliente.
        Order order = new Order();
        order.setClient(client);
        order.setItems(new ArrayList<>());

        // Guardar la entidad Order inicial (sin items) en la base de datos.
        Order savedOrder = orderRepository.save(order);

        // Crear los items del pedido basados en el DTO recibido.
        List<OrderItem> orderItems = orderDTO.getItems().stream().map(dto -> {
            Product product = products.stream().filter(p -> p.getId().equals(dto.getProductId())).findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with id: " + dto.getProductId())); // Lanza una excepción si no se encuentra el producto.
            return new OrderItem(null, product, dto.getQuantity(), dto.getPrice(), savedOrder); // Crea un nuevo OrderItem.
        }).collect(Collectors.toList());

        // Guardar los items del pedido en la base de datos.
        orderItemRepository.saveAll(orderItems);

        // Calcular el total del pedido sumando el precio de cada item por su cantidad.
        double total = orderItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        savedOrder.setTotal(total); // Asigna el total al pedido.
        orderRepository.save(savedOrder); // Actualiza el pedido con el total calculado.

        return OrderConvertTo.convertToDTO(savedOrder); // Convierte la entidad Order guardada a OrderDTO y la retorna.
    }

    /**
     * Elimina un pedido de la base de datos por su ID.
     *
     * @param id ID del pedido a eliminar.
     * @throws ResourceNotFoundException si el pedido con el ID especificado no se encuentra.
     * @return Ok
     */
    // Método para eliminar un pedido por su ID.
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id) // Recupera el pedido por su ID.
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id)); // Lanza una excepción si no se encuentra el pedido.
        orderRepository.delete(order); // Elimina el pedido de la base de datos.
    }
}

