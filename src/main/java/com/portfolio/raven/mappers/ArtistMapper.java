package com.portfolio.raven.mappers;

import com.portfolio.raven.dto.artistDto.ArtistDetail;
import com.portfolio.raven.dto.artistDto.ArtistListDto;
import com.portfolio.raven.dto.artistDto.RegisterArtistDto;
import com.portfolio.raven.dto.artistDto.UpdateArtistDto;
import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.entity.Genre;
import com.portfolio.raven.repository.ArtistRepository;
import com.portfolio.raven.repository.GenresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;


@Component
public class ArtistMapper {

     @Autowired
     private GenresRepository generoRepository;

     @Autowired
     private ArtistRepository artistRepository;


    @Transactional
    public Artist toEntity(RegisterArtistDto dto) {
        Artist artist = new Artist();
        artist.setName(dto.name());
        artist.setBio(dto.bio());

        Set<Genre> generos = dto.generos().stream()
                .map(id -> generoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Gênero não encontrado: " + id)))
                .collect(Collectors.toSet());

        artist.setGeneros(generos);

        return artist;
    }


    public void update(UpdateArtistDto dto, Artist artist) {
        if (dto.name() != null && !dto.name().isBlank()) {
            artist.setName(dto.name());
        }

        if (dto.name() != null && !dto.bio().isBlank()) {
            artist.setName(dto.bio());
        }
    }


    public ArtistDetail toDetailDto(Artist artist){
        Set<String> nomesGeneros = artist.getGeneros().stream()
                .map(Genre::getNome)
                .collect(Collectors.toSet());

        return new ArtistDetail(artist.getId(),artist.getName(), nomesGeneros,artist.getBio(),artist.getArtistImages(), artist.getCreated_at(), artist.getUpdate_at());
    }



    public ArtistListDto toList(Artist artist){
        Set<String> nomesGeneros = artist.getGeneros().stream()
                .map(Genre::getNome)
                .collect(Collectors.toSet());

        return  new ArtistListDto(artist.getId(),artist.getName(), nomesGeneros,artist.getBio(),artist.getArtistImages(),  artist.getCreated_at(), artist.getUpdate_at());
    }


}
