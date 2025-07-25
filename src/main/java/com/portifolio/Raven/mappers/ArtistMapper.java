package com.portifolio.Raven.mappers;

import com.portifolio.Raven.dto.artistDto.ArtistDetail;
import com.portifolio.Raven.dto.artistDto.ArtistListDto;
import com.portifolio.Raven.dto.artistDto.RegisterArtistDto;
import com.portifolio.Raven.dto.artistDto.UpdateArtistDto;
import com.portifolio.Raven.entity.Artist;
import com.portifolio.Raven.entity.Genero;
import com.portifolio.Raven.repository.ArtistRepository;
import com.portifolio.Raven.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
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


    @Transactional
    public ArtistDetail update(UUID id, UpdateArtistDto dto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));

        artist.updateArtist(dto);

        if (dto.generos() != null) {
            Set<Genero> generos = dto.generos().stream()
                    .map(gid -> generoRepository.findById(gid)
                            .orElseThrow(() -> new RuntimeException("Gênero não encontrado: " + gid)))
                    .collect(Collectors.toSet());
            artist.setGeneros(generos);
        }

        return new ArtistDetail(artist);
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
