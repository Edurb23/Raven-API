package com.portifolio.Raven.dto.userDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdatePassword(
        @NotBlank
        @Schema(description = "password", example = "Enter yours current password")
        String password,
        @Schema(description = "newPassword", example = "Enter a new secure password")
        @NotBlank
        String newPassoword
) {
}
