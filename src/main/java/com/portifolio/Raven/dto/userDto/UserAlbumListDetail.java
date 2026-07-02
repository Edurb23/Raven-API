package com.portifolio.Raven.dto.userDto;

import com.portifolio.Raven.dto.albumDto.AlbumDetail;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserAlbumListDetail(
        UUID id,
        UUID userId,
        String name,
        String description,
        List<AlbumDetail> albums,
        Instant created_at,
        Instant update_at
) {
}
