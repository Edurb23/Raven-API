package com.portfolio.raven.dto.artistDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import java.util.UUID;

public record UpdateArtistDto(


        @Schema(description = "name", example = "Artist or band name")
        String name,

        @Schema(description = "genres", example = "List of genre IDs")
        Set<UUID> genres,

        @Schema(description = "bio", example = "Artist biography")
        String bio
) {
}
