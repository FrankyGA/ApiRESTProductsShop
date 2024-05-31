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
import org.springframework.web.bind.annotation.RestController;

import com.spring.productsshop.dto.ClientDTO;
import com.spring.productsshop.exception.ErrorDetails;
import com.spring.productsshop.exception.ResourceNotFoundException;
import com.spring.productsshop.exception.ValidationException;
import com.spring.productsshop.mapper.ClientConvertTo;
import com.spring.productsshop.model.Client;
import com.spring.productsshop.service.ClientService;

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

    private final ClientService clientService; 

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // -------------- Métodos peticiones Consultar --------------//

    // -------------- Petición todos los clientes --------------//

    // Petición GET para obtener todos los clientes
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
        // Llama al servicio para obtener todos los clientes
        List<Client> clients = clientService.getAllClients();
        // Si no se encuentran clientes, lanza una excepción de recurso no encontrado
        if (clients.isEmpty()) {
            throw new ResourceNotFoundException("No clients found");
        }
        // Convierte la lista de clientes en una lista de DTO de clientes y la devuelve en una ResponseEntity
        return ResponseEntity.ok(ClientConvertTo.convertToDTOList(clients));
    }

    // -------------- Petición cliente por ID --------------//

    // Petición GET para obtener un cliente por su ID
    @Operation(summary = "Get client by ID", description = "Get a client by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found", content = {
                    @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Client not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        // Llama al servicio para obtener un cliente por su ID
        Optional<Client> clientOptional = clientService.getClientById(id);
        // Si no se encuentra el cliente, lanza una excepción de recurso no encontrado
        Client client = clientOptional
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        // Convierte el cliente en un DTO de cliente y lo devuelve en una ResponseEntity
        return ResponseEntity.ok(ClientConvertTo.convertToDTO(client));
    }

    // -------------- Métodos peticiones Insertar --------------//

    // Petición POST para crear un nuevo cliente
    @Operation(summary = "Create a new client", description = "Create a new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created"),
            @ApiResponse(responseCode = "400", description = "Invalid client data provided", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PostMapping("/clients")
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        // Si los datos del cliente son inválidos, lanza una excepción de validación
        if (clientDTO == null) {
            throw new ValidationException("Invalid client data provided");
        }
        // Convierte el DTO de cliente en una entidad de cliente
        Client client = ClientConvertTo.convertToEntity(clientDTO);
        // Crea un nuevo cliente en la base de datos
        Client savedClient = clientService.createClient(client);
        // Convierte el cliente guardado en un DTO de cliente y lo devuelve en una ResponseEntity con el código de estado 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientConvertTo.convertToDTO(savedClient));
    }

    // -------------- Métodos peticiones Modificar --------------//

    // -------------- Petición modificación total cliente --------------//

    // Petición PUT para actualizar un cliente por su ID
    @Operation(summary = "Update client by ID", description = "Update an existing client by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated"),
            @ApiResponse(responseCode = "404", description = "Client not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PutMapping("/clients/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO updatedClientDTO) {
        // Convierte el DTO de cliente actualizado en una entidad de cliente
        Client updatedClient = ClientConvertTo.convertToEntity(updatedClientDTO);
        // Actualiza el cliente en la base de datos
        Client savedClient = clientService.updateClient(id, updatedClient);
        // Devuelve el cliente actualizado en formato DTO en una ResponseEntity
        return ResponseEntity.ok(ClientConvertTo.convertToDTO(savedClient));
    }

    // -------------- Petición modificación de atributo cliente --------------//

    // Método PATCH para actualizar solo el nombre de un cliente
    @Operation(summary = "Update client name by ID", description = "Update the name of an existing client by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client name updated"),
            @ApiResponse(responseCode = "404", description = "Client not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PatchMapping("/clients/{id}/updateName")
    public ResponseEntity<ClientDTO> updateClientName(@PathVariable Long id, @RequestBody String newName) {
        // Actualiza el nombre del cliente en la base de datos
        Client savedClient = clientService.updateClientName(id, newName);
        // Devuelve el cliente actualizado en formato DTO en una ResponseEntity
        return ResponseEntity.ok(ClientConvertTo.convertToDTO(savedClient));
    }

    // -------------- Métodos peticiones Borrar --------------//

    // Petición DELETE para eliminar un cliente por su ID
    @Operation(summary = "Delete client by ID", description = "Delete a client by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted"),
            @ApiResponse(responseCode = "404", description = "Client not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        // Elimina el cliente de la base de datos
        clientService.deleteClient(id);
        // Devuelve una ResponseEntity sin contenido con el código de estado 204 (NO CONTENT)
        return ResponseEntity.noContent().build();
    }
}










