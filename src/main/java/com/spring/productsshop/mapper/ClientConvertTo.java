package com.spring.productsshop.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

import com.spring.productsshop.dto.ClientDTO;
import com.spring.productsshop.model.Client;


public class ClientConvertTo {

    private static final ModelMapper modelMapper = new ModelMapper();

    // ---------------- Client ---------------- //

    // Convierte una entidad Client a un objeto ClientDTO
    public static ClientDTO convertToDTO(Client client) {
        return modelMapper.map(client, ClientDTO.class);
    }

    // Convierte un objeto ClientDTO a una entidad Client
    public static Client convertToEntity(ClientDTO clientDTO) {
        return modelMapper.map(clientDTO, Client.class);
    }

    // Convierte una lista de entidades Client a una lista de objetos ClientDTO
    public static List<ClientDTO> convertToDTOList(List<Client> clients) {
        return clients.stream()
                .map(ClientConvertTo::convertToDTO)
                .collect(Collectors.toList());
    }
}

