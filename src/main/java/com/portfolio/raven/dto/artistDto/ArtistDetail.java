package com.portfolio.raven.dto.artistDto;

import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.entity.ArtistImage;
import com.portfolio.raven.entity.Genre;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ArtistDetail(UUID id, String name, Set<String> genres, String bio, List<ArtistImage>artistImages, Instant created_at, Instant update_at) {

   public ArtistDetail(Artist artist){
        this(artist.getId(), artist.getName(), artist.getGenres().stream().map(Genre::getName).collect(Collectors.toSet()), artist.getBio(), artist.getArtistImages(), artist.getCreated_at(), artist.getUpdate_at());
   }

}
