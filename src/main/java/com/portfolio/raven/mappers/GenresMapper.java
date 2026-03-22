package com.portfolio.raven.mappers;

import com.portfolio.raven.dto.generoDto.ArtistGenresDto;
import com.portfolio.raven.dto.generoDto.GeneroList;
import com.portfolio.raven.dto.generoDto.GenreDetail;
import com.portfolio.raven.dto.generoDto.RegisterGenero;
import com.portfolio.raven.entity.Genre;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GenresMapper {

   public Genre toEntity(RegisterGenero dto){
       Genre genres = new Genre();
       genres.setName(dto.name());
       return genres;
   }

   public GenreDetail toDetailDto(Genre genres){
       return new GenreDetail(genres.getId(),genres.getName(),genres.getArtists());
   }

    public GeneroList toList(Genre genres) {
        Set<ArtistGenresDto> artistDtos = genres.getArtists().stream()
                .map(artist -> new ArtistGenresDto(artist.getId(), artist.getName()))
                .collect(Collectors.toSet());

        return new GeneroList(genres.getId(), genres.getName(), artistDtos);
    }



}
