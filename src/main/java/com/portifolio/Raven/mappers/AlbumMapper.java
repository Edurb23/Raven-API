package com.portifolio.Raven.mappers;

import com.portifolio.Raven.dto.albumDto.AlbumDetail;
import com.portifolio.Raven.dto.albumDto.RegisterAlbumDto;
import com.portifolio.Raven.dto.albumDto.UpdateAlbumDto;
import com.portifolio.Raven.entity.Album;
import com.portifolio.Raven.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlbumMapper {

    @Autowired
    private ArtistRepository artistRepository;

    public Album toEntity(RegisterAlbumDto dto) {
        Album album = new Album();
        album.setCoverUrl(dto.coverUrl());
        album.setName(dto.name());
        album.setArtist(artistRepository.findById(dto.artistId())
                .orElseThrow(() -> new RuntimeException("Artist not found: " + dto.artistId())));
        album.setDurationSeconds(dto.durationSeconds());
        album.setType(dto.type());
        album.setReleaseYear(dto.releaseYear());
        return album;
    }

    public void update(UpdateAlbumDto dto, Album album) {
        if (dto.coverUrl() != null && !dto.coverUrl().isBlank()) {
            album.setCoverUrl(dto.coverUrl());
        }
        if (dto.name() != null && !dto.name().isBlank()) {
            album.setName(dto.name());
        }
        if (dto.artistId() != null) {
            album.setArtist(artistRepository.findById(dto.artistId())
                    .orElseThrow(() -> new RuntimeException("Artist not found: " + dto.artistId())));
        }
        if (dto.durationSeconds() != null) {
            album.setDurationSeconds(dto.durationSeconds());
        }
        if (dto.type() != null) {
            album.setType(dto.type());
        }
        if (dto.releaseYear() != null) {
            album.setReleaseYear(dto.releaseYear());
        }
    }

    public AlbumDetail toDetail(Album album) {
        return new AlbumDetail(
                album.getId(),
                album.getCoverUrl(),
                album.getName(),
                album.getArtist().getId(),
                album.getArtist().getNomeArtist(),
                album.getDurationSeconds(),
                album.getType(),
                album.getReleaseYear(),
                album.getCreated_at(),
                album.getUpdate_at()
        );
    }
}
