package com.portfolio.raven.service;

import com.portfolio.raven.entity.Album;
import com.portfolio.raven.entity.AlbumCover;
import com.portfolio.raven.repository.AlbumCoverRepository;
import com.portfolio.raven.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumCoverServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private AlbumCoverRepository albumCoverRepository;

    @InjectMocks
    private AlbumCoverService albumCoverService;

    @Test
    void shouldSaveFirstCoverAsSelectedAndUpdateAlbumCoverUrl() {
        UUID albumId = UUID.randomUUID();
        Album album = album(albumId, "OK Computer", "old-cover");
        MockMultipartFile file = new MockMultipartFile("file", "cover".getBytes());

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));
        when(albumCoverRepository.findByAlbumIdAndSelectedTrue(albumId)).thenReturn(Optional.empty());

        String response = albumCoverService.saveCoverAsBase64(file, albumId);

        assertEquals("Cover successfully saved for album: OK Computer", response);
        assertEquals("Y292ZXI=", album.getCoverUrl());
        verify(albumCoverRepository).save(any(AlbumCover.class));
        verify(albumRepository).save(album);
    }

    @Test
    void shouldSelectOnlyRequestedAlbumCoverAndUpdateAlbumCoverUrl() {
        UUID albumId = UUID.randomUUID();
        UUID coverId = UUID.randomUUID();
        Album album = album(albumId, "OK Computer", "old-cover");
        AlbumCover selected = cover(coverId, album, false, "new-cover");
        AlbumCover other = cover(UUID.randomUUID(), album, true, "old-cover");

        when(albumCoverRepository.findById(coverId)).thenReturn(Optional.of(selected));
        when(albumCoverRepository.findByAlbumId(albumId)).thenReturn(List.of(selected, other));

        String response = albumCoverService.selectAlbumCover(albumId, coverId);

        assertEquals("Selected cover updated for album: OK Computer", response);
        assertTrue(selected.getSelected());
        assertFalse(other.getSelected());
        assertEquals("new-cover", album.getCoverUrl());
        verify(albumCoverRepository).save(selected);
        verify(albumCoverRepository).save(other);
        verify(albumRepository).save(album);
    }

    private Album album(UUID albumId, String name, String coverUrl) {
        Album album = new Album();
        album.setId(albumId);
        album.setName(name);
        album.setCoverUrl(coverUrl);
        return album;
    }

    private AlbumCover cover(UUID coverId, Album album, boolean selected, String url) {
        AlbumCover cover = new AlbumCover();
        cover.setId(coverId);
        cover.setAlbum(album);
        cover.setSelected(selected);
        cover.setUrlImage(url);
        return cover;
    }
}
