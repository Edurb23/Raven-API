package com.portfolio.raven.dto.musicDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UpdateMusicDto(
        @Schema(description = "Music name", example = "Blinding Lights")
        String name,

        @Schema(description = "Artist UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID artistId,

        @Schema(description = "Album UUID. Use null to keep unchanged.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID albumId,

        @Schema(description = "Music duration in seconds", example = "200")
        Integer durationSeconds,

        @Schema(description = "Whether the music is a single", example = "false")
        Boolean single,

        @Schema(description = "Track number inside the album", example = "9")
        Integer trackNumber
) {
}
