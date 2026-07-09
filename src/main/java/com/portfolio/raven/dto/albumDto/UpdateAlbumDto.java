package com.portfolio.raven.dto.albumDto;

import com.portfolio.raven.entity.AlbumType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UpdateAlbumDto(
        @Schema(description = "Album cover image URL", example = "https://cdn.example.com/covers/album-deluxe.jpg")
        String coverUrl,

        @Schema(description = "Album name", example = "After Hours Deluxe")
        String name,

        @Schema(description = "Artist UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID artistId,

        @Schema(description = "Album duration in seconds", example = "4210")
        Integer durationSeconds,

        @Schema(description = "Album type", example = "LP")
        AlbumType type,

        @Schema(description = "Release year", example = "2020")
        Integer releaseYear
) {
}
