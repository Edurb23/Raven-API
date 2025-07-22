package com.portifolio.Raven.service;

import com.portifolio.Raven.dto.artistDto.ArtistDetail;
import com.portifolio.Raven.dto.artistDto.ArtistListDto;
import com.portifolio.Raven.dto.artistDto.RegisterArtistDto;
import com.portifolio.Raven.dto.artistDto.UpdateArtistDto;
import com.portifolio.Raven.mappers.ArtistMapper;
import com.portifolio.Raven.entity.Artist;
import com.portifolio.Raven.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistMapper artistMapper;


    public List<ArtistListDto> listAll(Pageable pageable){
        return artistRepository.findAll(pageable)
                .stream()
                .map(artistMapper::toList)
                .toList();
    }

    public ArtistDetail findById(UUID id) {
        var artist = artistRepository.getReferenceById(id);
        return artistMapper.toDetailDto(artist);
    }

    public ArtistDetail register(RegisterArtistDto dto){
        boolean exists = artistRepository.existsByNomeArtistIgnoreCase(dto.nomeArtist());

        if (exists) {
            throw new RuntimeException("Já existe um artista cadastrado com o nome: " + dto.nomeArtist());
        }

        Artist artist = artistMapper.toEntity(dto);
        artistRepository.save(artist);
        return artistMapper.toDetailDto(artist);
    }


    public Artist update(UUID id, UpdateArtistDto dto) {
        var artist = artistRepository.getReferenceById(id);
        artist.updateArtist(dto);
        return artist;
    }

    public void delete(UUID id) {
        artistRepository.deleteById(id);
    }






}
