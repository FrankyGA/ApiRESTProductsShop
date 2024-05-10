package com.spring.productsshop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Users", description = "Users")
@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user") // Nombre de la columna en la tabla
	@Schema(example = "1", description = "ID for user")
	private Long id;
	
	@NotNull(message = "Name is required")
    @Size(min = 3, message = "Name must be at least 3 characters long")
	@Schema(example = "Alan", description = "Name for user")
	private String name;
	
	@NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
	@Schema(example = "pfwf564ffg7w", description = "Password for user")
	private String password;
}
