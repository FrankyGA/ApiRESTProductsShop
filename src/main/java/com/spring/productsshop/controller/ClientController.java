package com.spring.productsshop.controller;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.productsshop.dto.ClientDTO;
import com.spring.productsshop.exception.ErrorDetails;
import com.spring.productsshop.exception.ResourceNotFoundException;
import com.spring.productsshop.exception.ValidationException;
import com.spring.productsshop.mapper.ClientConvertTo;
import com.spring.productsshop.model.Client;
import com.spring.productsshop.repository.ClientRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Client", description = "Client controller with CRUD Operations")
public class ClientController {

	private final ClientRepository clientRepository;

	@Autowired
	public ClientController(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	// -------------- Métodos peticiones Consultar --------------//
	
	// -------------- Petición todos los clientes --------------//

	@Operation(summary = "Get all clients", description = "Get a list of all clients")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients found, retrieved clients", content = {
            		@Content(mediaType = "application/json",
            				array = @ArraySchema(schema = @Schema(implementation = Client.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Clients not found", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
	@GetMapping("/clients")
	public ResponseEntity<List<ClientDTO>> getAllClients() {
		List<Client> clients = clientRepository.findAll();
		if (clients.isEmpty()) {
            throw new ResourceNotFoundException("No clients found");
        }
		return ResponseEntity.ok(ClientConvertTo.convertToDTOList(clients));
	}

	// -------------- Petición cliente por ID --------------//
	
	@Operation(summary = "Get client by ID", description = "Get a client by its ID")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found", content = {
            		@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Client not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
	@GetMapping("/clients/{id}")
	public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
		Optional<Client> clientOptional = clientRepository.findById(id);
		Client client = clientOptional
				.orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
		return ResponseEntity.ok(ClientConvertTo.convertToDTO(client));
	}

	// -------------- Métodos peticiones Insertar --------------//
	
	// Petición para insertar nuevo cliente
	@Operation(summary = "Create a new client", description = "Create a new client")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created"),
            @ApiResponse(responseCode = "400", description = "Invalid client data provided", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
	@PostMapping("/clients")
	public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
		if (clientDTO == null) {
            throw new ValidationException("Invalid client data provided");
        }
		Client client = ClientConvertTo.convertToEntity(clientDTO);
		Client savedClient = clientRepository.save(client);
		return ResponseEntity.status(HttpStatus.CREATED).body(ClientConvertTo.convertToDTO(savedClient));
	}

	// -------------- Métodos peticiones Modificar --------------//
	
	// -------------- Petición modificación total cliente --------------//

	// Petición para modificar cliente
	@Operation(summary = "Update client by ID", description = "Update an existing client by its ID")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated"),
            @ApiResponse(responseCode = "404", description = "Client not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
	@PutMapping("/clients/{id}")
	public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO updatedClientDTO) {
		// Busca cliente por id para guardarlo en un objeto cliente
		Optional<Client> clientOptional = clientRepository.findById(id);
		Client client = clientOptional
				.orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
		// Guarda los atributos que se pasan en el cuerpo de la petición
		client.setName(updatedClientDTO.getName());
		client.setAddress(updatedClientDTO.getAddress());
		client.setAge(updatedClientDTO.getAge());
		// Guarda el cliente modificado y y convierte en DTO para enviar los datos a la
		// respuesta
		Client savedClient = clientRepository.save(client);
		return ResponseEntity.ok(ClientConvertTo.convertToDTO(savedClient));
	}

	// -------------- Petición modificación de atributo cliente --------------//
	
	// Método para actualizar solo el nombre de un cliente
	@Operation(summary = "Update client name by ID", description = "Update the name of an existing client by its ID")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client name updated"),
            @ApiResponse(responseCode = "404", description = "Client not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
	@PatchMapping("/clients/{id}/updateName")
	public ResponseEntity<ClientDTO> updateClientName(@PathVariable Long id, @RequestBody String newName) {
		Optional<Client> clientOptional = clientRepository.findById(id);
		Client client = clientOptional
				.orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
		// Actualizar solo el nombre del cliente
		client.setName(newName);
		// Guardar el cliente actualizado en la base de datos
		Client savedClient = clientRepository.save(client);
		// Retornar el cliente actualizado en formato DTO
		return ResponseEntity.ok(ClientConvertTo.convertToDTO(savedClient));
	}

	// -------------- Métodos peticiones Borrar --------------//

	@Operation(summary = "Delete client by ID", description = "Delete a client by its ID")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted"),
            @ApiResponse(responseCode = "404", description = "Client not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
	@DeleteMapping("/clients/{id}")
	public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
		if (clientRepository.existsById(id)) {
			clientRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			throw new ResourceNotFoundException("Client not found with id: " + id);
		}
	}
}


