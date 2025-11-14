package com.portifolio.Raven.dto.artistDto;

import com.portifolio.Raven.entity.Artist;
import com.portifolio.Raven.entity.ArtistImage;
import com.portifolio.Raven.entity.Genero;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ArtistListDto(UUID idd, String nameArtist, Set<String> generos, String biografia, List<ArtistImage>artistImages,
                            Instant created_at, Instant update_at) {

    public ArtistListDto(Artist artist){
        this(artist.getId(), artist.getNomeArtist(), artist.getGeneros().stream().map(Genero::getNome).collect(Collectors.toSet()), artist.getBiografia(), artist.getArtistImages(), artist.getCreated_at(),artist.getUpdate_at());
    }
}
