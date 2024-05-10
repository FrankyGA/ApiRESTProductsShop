package com.spring.productsshop.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.spring.productsshop.dto.ProductDTO;
import com.spring.productsshop.model.Product;

public class ProductConvertTo {
	
	private static final ModelMapper modelMapper = new ModelMapper();

    // Convierte una entidad User a un objeto UserDTO
    public static ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    // Convierte un objeto UserDTO a una entidad User
    public static Product convertToEntity(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    // Convierte una lista de entidades User a una lista de objetos UserDTO
    public static List<ProductDTO> convertToDTOList(List<Product> products) {
        return products.stream()
                .map(ProductConvertTo::convertToDTO)
                .collect(Collectors.toList());
    }
}
