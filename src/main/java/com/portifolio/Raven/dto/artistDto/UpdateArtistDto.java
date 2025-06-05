package com.portifolio.Raven.dto.artistDto;

import jakarta.validation.constraints.NotBlank;

public record UpdateArtistDto(
        @NotBlank
        String nomeArtist,
        @NotBlank
        String genero,
        @NotBlank
        String biografia
) {
}
