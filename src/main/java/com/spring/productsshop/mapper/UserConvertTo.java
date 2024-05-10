package com.spring.productsshop.mapper;

import org.modelmapper.ModelMapper;

import com.spring.productsshop.dto.UserDTO;
import com.spring.productsshop.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserConvertTo {

    private static final ModelMapper modelMapper = new ModelMapper();

    // Convierte una entidad User a un objeto UserDTO
    public static UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    // Convierte un objeto UserDTO a una entidad User
    public static User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    // Convierte una lista de entidades User a una lista de objetos UserDTO
    public static List<UserDTO> convertToDTOList(List<User> users) {
        return users.stream()
                .map(UserConvertTo::convertToDTO)
                .collect(Collectors.toList());
    }
}

