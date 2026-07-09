package com.portfolio.raven.dto.albumDto;

import com.portfolio.raven.entity.AlbumType;

import java.time.Instant;
import java.util.UUID;

public record AlbumDetail(
        UUID id,
        String coverUrl,
        String name,
        UUID artistId,
        String artistName,
        Integer durationSeconds,
        AlbumType type,
        Integer releaseYear,
        Instant created_at,
        Instant update_at
) {
}
