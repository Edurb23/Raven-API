package com.portfolio.raven.dto.generoDto;

import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.entity.Genre;

import java.util.Set;
import java.util.UUID;

public record GenreDetail(UUID id, String name, Set<Artist> artists) {

            public GenreDetail(Genre genres){
                this(genres.getId(),genres.getName(),genres.getArtists());
            }

}
