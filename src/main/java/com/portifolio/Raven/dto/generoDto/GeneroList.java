package com.portifolio.Raven.dto.generoDto;

import com.portifolio.Raven.entity.Artist;
import com.portifolio.Raven.entity.Genero;

import java.util.Set;
import java.util.UUID;

public record GeneroList(UUID id, String nome, Set<Artist> artists) {

     public GeneroList(Genero genero){
         this(genero.getId(),genero.getNome(),genero.getArtists());
     }

}
