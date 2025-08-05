package com.portifolio.Raven.service;

import com.portifolio.Raven.dto.artistDto.ArtistDetail;
import com.portifolio.Raven.dto.artistDto.ArtistListDto;
import com.portifolio.Raven.dto.artistDto.RegisterArtistDto;
import com.portifolio.Raven.dto.artistDto.UpdateArtistDto;
import com.portifolio.Raven.entity.Genero;
import com.portifolio.Raven.mappers.ArtistMapper;
import com.portifolio.Raven.entity.Artist;
import com.portifolio.Raven.repository.ArtistRepository;
import com.portifolio.Raven.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private GeneroRepository generoRepository;


    public List<ArtistListDto> listAll(Pageable pageable){
        return artistRepository.findAll(pageable)
                .stream()
                .map(artistMapper::toList)
                .toList();
    }

    public ArtistDetail findById(UUID id) {
        var artist = artistRepository.findWithGeneros(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));
        return artistMapper.toDetailDto(artist);
    }



    public ArtistDetail toDetailDto(Artist artist){
        return new ArtistDetail(artist);
    }

    @Transactional
    public ArtistDetail register(RegisterArtistDto dto){
        boolean exists = artistRepository.existsByNomeArtistIgnoreCase(dto.nomeArtist());

        if (exists) {
            throw new RuntimeException("Já existe um artista cadastrado com o nome: " + dto.nomeArtist());
        }

        Artist artist = artistMapper.toEntity(dto);
        artistRepository.save(artist);
        return artistMapper.toDetailDto(artist);
    }


    @Transactional
    public Artist update(UUID id, UpdateArtistDto dto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));

        artistMapper.update(dto, artist);

        if (dto.generos() != null) {
            Set<Genero> generos = dto.generos().stream()
                    .map(gid -> generoRepository.findById(gid)
                            .orElseThrow(() -> new RuntimeException("Gênero não encontrado: " + gid)))
                    .collect(Collectors.toSet());
            artist.setGeneros(generos);
        }

        return artist;
    }



    public void delete(UUID id) {
        artistRepository.deleteById(id);
    }






}
