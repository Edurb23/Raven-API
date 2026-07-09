package com.portfolio.raven.dto.userDto;

import java.time.Instant;
import java.util.UUID;

public record AlbumReviewDetail(
        UUID id,
        UUID userId,
        String username,
        UUID albumId,
        String albumName,
        Integer rating,
        String comment,
        Instant created_at,
        Instant update_at
) {
}
