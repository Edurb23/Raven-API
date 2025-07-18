package com.portifolio.Raven.mappers;

import com.portifolio.Raven.dto.artistDto.ArtistDetail;
import com.portifolio.Raven.dto.artistDto.ArtistListDto;
import com.portifolio.Raven.dto.artistDto.RegisterArtistDto;
import com.portifolio.Raven.entity.Artist;
import com.portifolio.Raven.entity.Genero;
import com.portifolio.Raven.repository.GeneroRepository;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class ArtistMapper {


    GeneroRepository generoRepository;

    public Artist toEntity(RegisterArtistDto dto){
        Artist artist = new Artist();
        artist.setNomeArtist(dto.nomeArtist());
        artist.setBiografia(dto.biografia());
        Set<Genero> generos = dto.generos().stream()
                .map(nomeGenero -> generoRepository.findByNome(nomeGenero)
                        .orElseThrow(() -> new RuntimeException("Gênero não encontrado: " + nomeGenero)))
                .collect(Collectors.toSet());
        artist.setGeneros(generos);

        return artist;
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
