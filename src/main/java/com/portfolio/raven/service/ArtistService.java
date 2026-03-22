package com.portfolio.raven.service;

import com.portfolio.raven.dto.artistDto.ArtistDetail;
import com.portfolio.raven.dto.artistDto.ArtistListDto;
import com.portfolio.raven.dto.artistDto.RegisterArtistDto;
import com.portfolio.raven.dto.artistDto.UpdateArtistDto;
import com.portfolio.raven.entity.Genre;
import com.portfolio.raven.mappers.ArtistMapper;
import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.repository.ArtistRepository;
import com.portfolio.raven.repository.GenresRepository;
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
    private GenresRepository genreRepository;


    public List<ArtistListDto> listAll(Pageable pageable){
        return artistRepository.findAll(pageable)
                .stream()
                .map(artistMapper::toList)
                .toList();
    }

    public ArtistDetail findById(UUID id) {
        var artist = artistRepository.findWithGenres(id)
                .orElseThrow(() -> new RuntimeException("Artist or band not found."));
        return artistMapper.toDetailDto(artist);
    }



    public ArtistDetail toDetailDto(Artist artist){
        return new ArtistDetail(artist);
    }

    @Transactional
    public ArtistDetail register(RegisterArtistDto dto){
        boolean exists = artistRepository.existsByNameIgnoreCase(dto.name());

        if (exists) {
            throw new RuntimeException("An artist or band with this name already exists " + dto.name());
        }

        Artist artist = artistMapper.toEntity(dto);
        artistRepository.save(artist);
        return artistMapper.toDetailDto(artist);
    }


    @Transactional
    public Artist update(UUID id, UpdateArtistDto dto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist or band not found."));

        artistMapper.update(dto, artist);

        if (dto.genres() != null) {
            Set<Genre> genres = dto.genres().stream()
                    .map(gid -> genreRepository.findById(gid)
                            .orElseThrow(() -> new RuntimeException("Genre not found. " + gid)))
                    .collect(Collectors.toSet());
            artist.setGenres(genres);
        }

        return artist;
    }



    public void delete(UUID id) {
        artistRepository.deleteById(id);
    }






}
