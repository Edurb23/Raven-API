package com.portifolio.Raven.dto.artistDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record RegisterArtistDto(

        @NotBlank
        String nomeArtist,
        @NotEmpty
        Set<UUID> generos,
        @NotBlank
        String biografia
) {
}
