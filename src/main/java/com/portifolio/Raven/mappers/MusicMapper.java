package com.portifolio.Raven.mappers;

import com.portifolio.Raven.dto.musicDto.MusicDetail;
import com.portifolio.Raven.dto.musicDto.RegisterMusicDto;
import com.portifolio.Raven.dto.musicDto.UpdateMusicDto;
import com.portifolio.Raven.entity.Album;
import com.portifolio.Raven.entity.Music;
import com.portifolio.Raven.repository.AlbumRepository;
import com.portifolio.Raven.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicMapper {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public Music toEntity(RegisterMusicDto dto) {
        Music music = new Music();
        music.setName(dto.name());
        music.setArtist(artistRepository.findById(dto.artistId())
                .orElseThrow(() -> new RuntimeException("Artist not found: " + dto.artistId())));
        music.setAlbum(findAlbum(dto.albumId()));
        music.setDurationSeconds(dto.durationSeconds());
        music.setSingle(dto.single() != null ? dto.single() : dto.albumId() == null);
        music.setTrackNumber(dto.trackNumber());
        return music;
    }

    public void update(UpdateMusicDto dto, Music music) {
        if (dto.name() != null && !dto.name().isBlank()) {
            music.setName(dto.name());
        }
        if (dto.artistId() != null) {
            music.setArtist(artistRepository.findById(dto.artistId())
                    .orElseThrow(() -> new RuntimeException("Artist not found: " + dto.artistId())));
        }
        if (dto.albumId() != null) {
            music.setAlbum(findAlbum(dto.albumId()));
        }
        if (dto.durationSeconds() != null) {
            music.setDurationSeconds(dto.durationSeconds());
        }
        if (dto.single() != null) {
            music.setSingle(dto.single());
        }
        if (dto.trackNumber() != null) {
            music.setTrackNumber(dto.trackNumber());
        }
    }

    public MusicDetail toDetail(Music music) {
        Album album = music.getAlbum();
        return new MusicDetail(
                music.getId(),
                music.getName(),
                music.getArtist().getId(),
                music.getArtist().getNomeArtist(),
                album != null ? album.getId() : null,
                album != null ? album.getName() : null,
                music.getDurationSeconds(),
                music.getSingle(),
                music.getTrackNumber(),
                music.getCreated_at(),
                music.getUpdate_at()
        );
    }

    private Album findAlbum(java.util.UUID albumId) {
        if (albumId == null) {
            return null;
        }
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found: " + albumId));
    }
}
