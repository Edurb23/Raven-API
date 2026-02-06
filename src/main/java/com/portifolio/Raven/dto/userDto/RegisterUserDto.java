package com.portifolio.Raven.dto.userDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserDto(

        @NotBlank
        @Schema(description = "username", example = "Choose a username (not a real name)")
        String username,
        @NotBlank
        @Schema(description = "email", example = "user@email.com")
        String email,
        @NotBlank
        @Schema(description = "password", example = "Enter a secure password")
        String password






) {
}
