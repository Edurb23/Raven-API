package com.portfolio.raven.dto.artistDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.UUID;

public record RegisterArtistDto(

        @NotBlank
        @Schema(description = "name", example = "Artist or band name")
        String name,
        @NotEmpty
        @Schema(description = "genres", example = "List of genre IDs")
        Set<UUID> genres,
        @NotBlank
        @Schema(description = "bio", example = "Artist biography")
        String bio
) {
}
