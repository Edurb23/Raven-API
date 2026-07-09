package com.portfolio.raven.dto.albumDto;

import com.portfolio.raven.entity.AlbumType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegisterAlbumDto(
        @NotBlank
        @Schema(description = "Album cover image URL", example = "https://cdn.example.com/covers/album.jpg")
        String coverUrl,

        @NotBlank
        @Schema(description = "Album name", example = "After Hours")
        String name,

        @NotNull
        @Schema(description = "Artist UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID artistId,

        @NotNull
        @Min(1)
        @Schema(description = "Album duration in seconds", example = "3372")
        Integer durationSeconds,

        @NotNull
        @Schema(description = "Album type", example = "LP")
        AlbumType type,

        @NotNull
        @Min(1900)
        @Schema(description = "Release year", example = "2020")
        Integer releaseYear
) {
}
