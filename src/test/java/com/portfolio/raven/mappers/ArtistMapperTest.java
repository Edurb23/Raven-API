package com.portfolio.raven.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.entity.ArtistImage;
import org.junit.jupiter.api.Test;

import java.util.AbstractList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ArtistMapperTest {
    @Test
    void listImagesRemainSerializableAfterPersistenceSessionCloses() throws Exception {
        var sessionOpen = new AtomicBoolean(true);
        var image = new ArtistImage();
        image.setUrlImage("data:image/png;base64,abc");
        var artist = new Artist();
        artist.setGenres(Set.of());
        artist.setArtistImages(new AbstractList<>() {
            @Override
            public ArtistImage get(int index) {
                checkSession();
                return image;
            }

            @Override
            public int size() {
                checkSession();
                return 1;
            }

            private void checkSession() {
                if (!sessionOpen.get()) {
                    throw new IllegalStateException("Persistence session is closed");
                }
            }
        });

        var dto = new ArtistMapper().toList(artist);
        sessionOpen.set(false);

        var json = new ObjectMapper().writeValueAsString(dto);
        assertTrue(json.contains("data:image/png;base64,abc"));
    }
}
