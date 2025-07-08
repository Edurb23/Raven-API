package com.portifolio.Raven.dto.artistDto;

import com.portifolio.Raven.model.Artist;

import java.util.UUID;

public record ArtistListDto(UUID idd, String nameArtist, String genero, String biografia) {

    public ArtistListDto(Artist artist){
        this(artist.getId(), artist.getNomeArtist(), artist.getGenero(), artist.getBiografia());
    }
}
