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

import com.spring.productsshop.dto.UserDTO;
import com.spring.productsshop.exception.ErrorDetails;
import com.spring.productsshop.exception.ResourceNotFoundException;
import com.spring.productsshop.exception.UsernameAlreadyExistsException;
import com.spring.productsshop.mapper.UserConvertTo;
import com.spring.productsshop.model.User;
import com.spring.productsshop.repository.UserRepository;
import com.spring.productsshop.service.UserService;
import com.spring.productsshop.util.PasswordUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;

@RestController
@Tag(name = "User", description = "User controller with CRUD Operations")
public class UserController {

	private final UserService userService;

	// Constructor que inyecta el servicio UserService
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	// ---------------------Método para obtener todos los usuarios---------------------//
	@Operation(summary = "Get all users", description = "Get a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found, retrieved users", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Users not found", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		// Guarda todos los usuarios en una lista
		List<User> users = userService.getAllUsers();
		//Si no se encuentran ususarios lanza excepcion
		if (users.isEmpty()) {
			throw new ResourceNotFoundException("No users found");
		}
		// Retorna una lista de usuarios convertidos a DTO
		return ResponseEntity.ok(UserConvertTo.convertToDTOList(users)); 
	}

	// ---------------------Método para obtener un usuario por su ID---------------------//
	@Operation(summary = "Get user by ID", description = "Get a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
	@GetMapping("/users/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
		// Guarda el usuario encontrado en un optional
		Optional<User> userOptional = userService.findById(id);
		// Lanza una excepción si no se encuentra
		User user = userOptional.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		return ResponseEntity.ok(UserConvertTo.convertToDTO(user)); // Retorna el usuario convertido a DTO
	}

	// ---------------------Método para crear un nuevo usuario---------------------//
	@Operation(summary = "Create a new user", description = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "409", description = "Username already exists", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
	@PostMapping("/users")
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
		if (userDTO == null || userDTO.getName().length() < 3 || userDTO.getPassword().length() < 8) {
			// Lanza una excepción si los datos del usuario son inválidos
			throw new ValidationException("Invalid user data provided"); 																
		}
		User user = userService.createUser(userDTO); // Crea un nuevo usuario
		// Retorna el usuario creado como DTO
		return ResponseEntity.status(HttpStatus.CREATED).body(UserConvertTo.convertToDTO(user)); 																					
	}

	// ---------------------Método para actualizar un usuario existente por su ID---------------------//
	@Operation(summary = "Update user by ID", description = "Update an existing user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
	@PutMapping("/users/{id}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO updatedUserDTO) {
		Optional<User> userOptional = userService.findById(id);
		// Lanza una excepción si no se encuentra
		User user = userOptional.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)); 
		user.setName(updatedUserDTO.getName());
		// Actualiza el nombre y la contraseña del usuario
		user.setPassword(PasswordUtil.hashPassword(updatedUserDTO.getPassword())); 
		User savedUser = userService.save(user); // Guarda los cambios en el usuario
		return ResponseEntity.ok(UserConvertTo.convertToDTO(savedUser)); // Retorna el usuario actualizado como DTO
	}

	// ---------------------Método para eliminar un usuario por su ID---------------------//
	@Operation(summary = "Delete user by ID", description = "Delete a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found with id: ...", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		if (userService.existsById(id)) {
			userService.deleteById(id); // Elimina el usuario si existe
			return ResponseEntity.noContent().build(); // Retorna una respuesta sin contenido
		} else {
			// Lanza una excepción si no se encuentra el usuario
			throw new ResourceNotFoundException("User not found with id: " + id);
		}
	}

	/*
	 * @PostMapping("/api/login") public ResponseEntity<?> login(@RequestBody
	 * UserDTO userDTO) { if (userService.checkCredentials(userDTO.getName(),
	 * userDTO.getPassword())) { return ResponseEntity.ok("Login successful"); }
	 * else { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
	 * body("Invalid username or password"); } }
	 */
}
