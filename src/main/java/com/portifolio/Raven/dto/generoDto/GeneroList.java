package com.portifolio.Raven.dto.generoDto;

import com.portifolio.Raven.entity.Artist;
import com.portifolio.Raven.entity.Genero;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record GeneroList(UUID id, String nome, Set<ArtistGeneroDto> artists) {

     public GeneroList(Genero genero){
         this(genero.getId(),genero.getNome(),   genero.getArtists().stream()
                 .map(artist -> new ArtistGeneroDto(artist.getId(), artist.getNomeArtist()))
                 .collect(Collectors.toSet()));
     }

}
