package com.portfolio.raven.mappers;

import com.portfolio.raven.dto.artistDto.ArtistDetail;
import com.portfolio.raven.dto.artistDto.ArtistListDto;
import com.portfolio.raven.dto.artistDto.RegisterArtistDto;
import com.portfolio.raven.dto.artistDto.UpdateArtistDto;
import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.entity.Genero;
import com.portfolio.raven.repository.ArtistRepository;
import com.portfolio.raven.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;


@Component
public class ArtistMapper {

     @Autowired
     private GeneroRepository generoRepository;

     @Autowired
     private ArtistRepository artistRepository;


    @Transactional
    public Artist toEntity(RegisterArtistDto dto) {
        Artist artist = new Artist();
        artist.setNomeArtist(dto.nomeArtist());
        artist.setBiografia(dto.biografia());

        Set<Genero> generos = dto.generos().stream()
                .map(id -> generoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Gênero não encontrado: " + id)))
                .collect(Collectors.toSet());

        artist.setGeneros(generos);

        return artist;
    }


    public void update(UpdateArtistDto dto, Artist artist) {
        if (dto.nomeArtist() != null && !dto.nomeArtist().isBlank()) {
            artist.setNomeArtist(dto.nomeArtist());
        }

        if (dto.biografia() != null && !dto.biografia().isBlank()) {
            artist.setBiografia(dto.biografia());
        }
    }


    public ArtistDetail toDetailDto(Artist artist){
        Set<String> nomesGeneros = artist.getGeneros().stream()
                .map(Genero::getNome)
                .collect(Collectors.toSet());

        return new ArtistDetail(artist.getId(),artist.getNomeArtist(), nomesGeneros,artist.getBiografia(),artist.getArtistImages(), artist.getCreated_at(), artist.getUpdate_at());
    }



    public ArtistListDto toList(Artist artist){
        Set<String> nomesGeneros = artist.getGeneros().stream()
                .map(Genero::getNome)
                .collect(Collectors.toSet());

        return  new ArtistListDto(artist.getId(),artist.getNomeArtist(), nomesGeneros,artist.getBiografia(),artist.getArtistImages(),  artist.getCreated_at(), artist.getUpdate_at());
    }


}
