package com.portfolio.raven.dto.generoDto;

import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.entity.Genero;

import java.util.Set;
import java.util.UUID;

public record GeneroDetail(UUID id, String nome, Set<Artist> artists) {

            public GeneroDetail(Genero genero){
                this(genero.getId(),genero.getNome(),genero.getArtists());
            }

}
