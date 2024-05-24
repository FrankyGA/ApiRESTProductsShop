package com.spring.productsshop.exception;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
	@Schema(example = "date", description = "date exception")
    private LocalDateTime timestamp;
	@Schema(example = "error code", description = "error code")
    private int status;
	@Schema(example = "error", description = "type of error")
    private String error;
	@Schema(example = "not found", description = "message error")
    private String message;
	@Schema(example = "**path**", description = "path URI")
    private String path;

}

