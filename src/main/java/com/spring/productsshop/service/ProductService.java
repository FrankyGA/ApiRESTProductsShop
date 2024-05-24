package com.spring.productsshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.productsshop.exception.ResourceNotFoundException;
import com.spring.productsshop.model.Product;
import com.spring.productsshop.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Inyección de dependencias de ProductRepository
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Obtiene todos los productos de la base de datos.
     *
     * @return Lista de todos los productos.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param id ID del producto a buscar.
     * @return Un Optional que contiene el producto si se encuentra, de lo contrario está vacío.
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Crea un nuevo producto en la base de datos.
     * Esta operación se ejecuta dentro de una transacción.
     *
     * @param product Producto a crear.
     * @return El producto creado.
     */
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Actualiza un producto existente en la base de datos.
     * Esta operación se ejecuta dentro de una transacción.
     *
     * @param id             ID del producto a actualizar.
     * @param updatedProduct Objeto que contiene los nuevos datos del producto.
     * @return El producto actualizado.
     * @throws ResourceNotFoundException si el producto con el ID especificado no se encuentra.
     */
    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    // Actualiza los campos del producto con los nuevos datos
                    product.setName(updatedProduct.getName());
                    product.setBrand(updatedProduct.getBrand());
                    product.setPrice(updatedProduct.getPrice());
                    // Guarda el producto actualizado en la base de datos
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    /**
     * Elimina un producto de la base de datos por su ID.
     * Esta operación se ejecuta dentro de una transacción.
     *
     * @param id ID del producto a eliminar.
     * @throws ResourceNotFoundException si el producto con el ID especificado no se encuentra.
     */
    @Transactional
    public void deleteProduct(Long id) {
        // Verifica si el producto existe en la base de datos
        if (productRepository.existsById(id)) {
            // Elimina el producto de la base de datos
            productRepository.deleteById(id);
        } else {
            // Lanza una excepción si el producto no se encuentra
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
    }
}