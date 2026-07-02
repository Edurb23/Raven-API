package com.portifolio.Raven.dto.musicDto;

import java.time.Instant;
import java.util.UUID;

public record MusicDetail(
        UUID id,
        String name,
        UUID artistId,
        String artistName,
        UUID albumId,
        String albumName,
        Integer durationSeconds,
        Boolean single,
        Integer trackNumber,
        Instant created_at,
        Instant update_at
) {
}
