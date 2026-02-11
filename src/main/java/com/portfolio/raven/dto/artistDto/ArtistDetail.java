package com.portfolio.raven.dto.artistDto;

import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.entity.ArtistImage;
import com.portfolio.raven.entity.Genero;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ArtistDetail(UUID id, String nameArtist, Set<String> generos, String biografia, List<ArtistImage>artistImages, Instant created_at, Instant update_at) {

   public ArtistDetail(Artist artist){
        this(artist.getId(), artist.getNomeArtist(), artist.getGeneros().stream().map(Genero::getNome).collect(Collectors.toSet()), artist.getBiografia(), artist.getArtistImages(), artist.getCreated_at(), artist.getUpdate_at());
   }

}
