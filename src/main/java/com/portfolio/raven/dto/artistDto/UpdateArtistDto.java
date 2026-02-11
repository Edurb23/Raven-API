package com.portfolio.raven.dto.artistDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import java.util.UUID;

public record UpdateArtistDto(


        @Schema(description = "nomeArtists", example = "Artist or band name")
        String nomeArtist,

        @Schema(description = "generos", example = "List of genre IDs")
        Set<UUID> generos,

        @Schema(description = "biografia", example = "Artist biography")
        String biografia
) {
}
