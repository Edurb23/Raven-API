package com.portifolio.Raven.dto.artistDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.UUID;

public record RegisterArtistDto(

        @NotBlank
        @Schema(description = "nomeArtists", example = "Artist or band name")
        String nomeArtist,
        @NotEmpty
        @Schema(description = "generos", example = "List of genre IDs")
        Set<UUID> generos,
        @NotBlank
        @Schema(description = "biografia", example = "Artist biography")
        String biografia
) {
}
