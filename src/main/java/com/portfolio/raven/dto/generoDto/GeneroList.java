package com.portfolio.raven.dto.generoDto;

import com.portfolio.raven.entity.Genre;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record GeneroList(UUID id, String nome, Set<ArtistGenresDto> artists) {

     public GeneroList(Genre genero){
         this(genero.getId(),genero.getNome(),   genero.getArtists().stream()
                 .map(artist -> new ArtistGenresDto(artist.getId(), artist.getName()))
                 .collect(Collectors.toSet()));
     }

}
