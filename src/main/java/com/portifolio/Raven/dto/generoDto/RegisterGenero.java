package com.portifolio.Raven.dto.generoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RegisterGenero(
        @NotBlank
        @Schema(description = "nome", example = "Name of a new genre")
        String nome

) {
}
