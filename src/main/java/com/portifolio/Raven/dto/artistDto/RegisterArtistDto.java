package com.portifolio.Raven.dto.artistDto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;

public record RegisterArtistDto(

        @NotBlank
        String nomeArtist,

        Set<String> generos,
        @NotBlank
        String biografia
) {
}
