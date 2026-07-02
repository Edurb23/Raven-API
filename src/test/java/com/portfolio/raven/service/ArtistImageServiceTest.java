package com.portfolio.raven.service;

import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.entity.ArtistImage;
import com.portfolio.raven.repository.ArtistImageRepository;
import com.portfolio.raven.repository.ArtistRepository;
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
class ArtistImageServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ArtistImageRepository artistImageRepository;

    @InjectMocks
    private ArtistImageService artistImageService;

    @Test
    void shouldSaveFirstArtistImageAsSelected() {
        UUID artistId = UUID.randomUUID();
        Artist artist = artist(artistId, "Radiohead");
        MockMultipartFile file = new MockMultipartFile("file", "image".getBytes());

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));
        when(artistImageRepository.findByArtistIdAndSelectedTrue(artistId)).thenReturn(Optional.empty());

        String response = artistImageService.saveImageAsBase64(file, artistId);

        assertEquals("Image successfully saved for artist:  Radiohead", response);
        verify(artistImageRepository).save(any(ArtistImage.class));
    }

    @Test
    void shouldSelectOnlyRequestedArtistImage() {
        UUID artistId = UUID.randomUUID();
        UUID imageId = UUID.randomUUID();
        Artist artist = artist(artistId, "Radiohead");
        ArtistImage selected = artistImage(imageId, artist, false);
        ArtistImage other = artistImage(UUID.randomUUID(), artist, true);

        when(artistImageRepository.findById(imageId)).thenReturn(Optional.of(selected));
        when(artistImageRepository.findByArtistId(artistId)).thenReturn(List.of(selected, other));

        String response = artistImageService.selectArtistImage(artistId, imageId);

        assertEquals("Selected image updated for artist: Radiohead", response);
        assertTrue(selected.getSelected());
        assertFalse(other.getSelected());
        verify(artistImageRepository).save(selected);
        verify(artistImageRepository).save(other);
    }

    private Artist artist(UUID artistId, String name) {
        Artist artist = new Artist();
        artist.setId(artistId);
        artist.setName(name);
        return artist;
    }

    private ArtistImage artistImage(UUID imageId, Artist artist, boolean selected) {
        ArtistImage image = new ArtistImage();
        image.setId(imageId);
        image.setArtist(artist);
        image.setSelected(selected);
        image.setUrlImage("image");
        return image;
    }
}
