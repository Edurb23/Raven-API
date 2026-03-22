package com.portfolio.raven.dto.artistDto;

import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.entity.ArtistImage;
import com.portfolio.raven.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Schema(description = "DTO of artist")
public record ArtistListDto(UUID idd, String name, Set<String> genres, String bio, List<ArtistImage>artistImages,
                            Instant created_at, Instant update_at) {

    public ArtistListDto(Artist artist){
        this(artist.getId(), artist.getName(), artist.getGenres().stream().map(Genre::getName).collect(Collectors.toSet()), artist.getBio(), artist.getArtistImages(), artist.getCreated_at(),artist.getUpdate_at());
    }
}
