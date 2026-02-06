package com.portifolio.Raven.dto.userDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateEmailDto(
        @NotBlank
        @Schema(description = "email", example = "user@email.com")
        String email



) {
}
