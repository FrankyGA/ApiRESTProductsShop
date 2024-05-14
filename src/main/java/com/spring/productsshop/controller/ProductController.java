package com.spring.productsshop.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.productsshop.dto.ProductDTO;
import com.spring.productsshop.exception.ErrorDetails;
import com.spring.productsshop.exception.ResourceNotFoundException;
import com.spring.productsshop.exception.ValidationException;
import com.spring.productsshop.mapper.ProductConvertTo;
import com.spring.productsshop.model.Product;
import com.spring.productsshop.repository.ProductRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Product", description = "Product controller with CRUD Operations")
public class ProductController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Operation(summary = "Get all products", description = "Get a list of all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found, retrieved products", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Products not found", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found");
        }
        return ResponseEntity.ok(ProductConvertTo.convertToDTOList(products));
    }

    @Operation(summary = "Get product by ID", description = "Get a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return ResponseEntity.ok(ProductConvertTo.convertToDTO(product));
    }

    @Operation(summary = "Create a new product", description = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid product data provided", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        if (productDTO == null) {
            throw new ValidationException("Invalid product data provided");
        }
        Product product = ProductConvertTo.convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductConvertTo.convertToDTO(savedProduct));
    }

    @Operation(summary = "Update product by ID", description = "Update an existing product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO updatedProductDTO) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setName(updatedProductDTO.getName());
        product.setBrand(updatedProductDTO.getBrand());
        product.setPrice(updatedProductDTO.getPrice());
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(ProductConvertTo.convertToDTO(savedProduct));
    }

    @Operation(summary = "Delete product by ID", description = "Delete a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
    }
    
    
}

