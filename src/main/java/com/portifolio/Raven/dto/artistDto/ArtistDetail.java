package com.portifolio.Raven.dto.artistDto;

import com.portifolio.Raven.entity.Artist;
import com.portifolio.Raven.entity.ArtistImage;

import java.util.List;
import java.util.UUID;

public record ArtistDetail(UUID id, String nameArtist, String genero, String biografia, List<ArtistImage>artistImages) {

   public ArtistDetail(Artist artist){
        this(artist.getId(), artist.getNomeArtist(), artist.getGenero(), artist.getBiografia(), artist.getArtistImages());
   }

}
