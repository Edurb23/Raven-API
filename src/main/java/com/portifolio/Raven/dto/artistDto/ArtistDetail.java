package com.portifolio.Raven.dto.artistDto;

import com.portifolio.Raven.model.Artist;

public record ArtistDetail(Long id, String nameArtist, String genero, String biografia) {

   public ArtistDetail(Artist artist){
        this(artist.getId(), artist.getNomeArtist(), artist.getGenero(), artist.getBiografia());
   }

}
