package com.portifolio.Raven.dto.generoDto;

import jakarta.validation.constraints.NotBlank;

public record RegisterGenero(
        @NotBlank
        String nome

) {
}
