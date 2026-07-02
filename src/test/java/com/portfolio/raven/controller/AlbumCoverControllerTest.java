package com.portfolio.raven.controller;

import com.portfolio.raven.service.AlbumCoverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AlbumCoverControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AlbumCoverService albumCoverService;

    @InjectMocks
    private AlbumCoverController albumCoverController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(albumCoverController).build();
    }

    @Test
    void shouldUploadAlbumCover() throws Exception {
        UUID albumId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "cover.png",
                MediaType.IMAGE_PNG_VALUE,
                "cover-content".getBytes()
        );

        when(albumCoverService.saveCoverAsBase64(file, albumId))
                .thenReturn("Cover successfully saved for album: OK Computer");

        mockMvc.perform(multipart("/album/{albumId}/covers/upload", albumId).file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Cover successfully saved for album: OK Computer"));

        verify(albumCoverService).saveCoverAsBase64(file, albumId);
    }

    @Test
    void shouldSelectAlbumCover() throws Exception {
        UUID albumId = UUID.randomUUID();
        UUID coverId = UUID.randomUUID();

        when(albumCoverService.selectAlbumCover(albumId, coverId))
                .thenReturn("Selected cover updated for album: OK Computer");

        mockMvc.perform(put("/album/{albumId}/covers/{coverId}/select", albumId, coverId))
                .andExpect(status().isOk())
                .andExpect(content().string("Selected cover updated for album: OK Computer"));

        verify(albumCoverService).selectAlbumCover(albumId, coverId);
    }
}
