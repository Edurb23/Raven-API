package com.portifolio.Raven.dto.musicDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegisterMusicDto(
        @NotBlank
        @Schema(description = "Music name", example = "Blinding Lights")
        String name,

        @NotNull
        @Schema(description = "Artist UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID artistId,

        @Schema(description = "Album UUID. Leave null when the music is a single.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID albumId,

        @NotNull
        @Min(1)
        @Schema(description = "Music duration in seconds", example = "200")
        Integer durationSeconds,

        @Schema(description = "Whether the music is a single. If omitted, it is inferred from albumId.", example = "false")
        Boolean single,

        @Schema(description = "Track number inside the album", example = "9")
        Integer trackNumber
) {
}
