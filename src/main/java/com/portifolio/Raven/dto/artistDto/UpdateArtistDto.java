package com.portifolio.Raven.dto.artistDto;

import jakarta.validation.constraints.NotBlank;


import java.util.Set;

public record UpdateArtistDto(
        @NotBlank
        String nomeArtist,
        @NotBlank
        Set<String> generos,
        @NotBlank
        String biografia
) {
}
