package com.portfolio.raven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.raven.dto.artistDto.ArtistDetail;
import com.portfolio.raven.dto.artistDto.ArtistListDto;
import com.portfolio.raven.dto.artistDto.RegisterArtistDto;
import com.portfolio.raven.dto.artistDto.UpdateArtistDto;
import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.entity.Genre;
import com.portfolio.raven.repository.ArtistRepository;
import com.portfolio.raven.service.ArtistImageService;
import com.portfolio.raven.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ArtistControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ArtistImageService artistImageService;

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private ArtistController artistController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
    }

    @Test
    void shouldListArtists() throws Exception {
        UUID artistId = UUID.randomUUID();
        ArtistListDto artist = new ArtistListDto(
                artistId,
                "Radiohead",
                Set.of("Alternative Rock"),
                "English rock band",
                List.of(),
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-02T00:00:00Z")
        );

        when(artistService.listAll(any(Pageable.class))).thenReturn(List.of(artist));

        mockMvc.perform(get("/artist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idd").value(artistId.toString()))
                .andExpect(jsonPath("$[0].name").value("Radiohead"))
                .andExpect(jsonPath("$[0].genres[0]").value("Alternative Rock"))
                .andExpect(jsonPath("$[0].bio").value("English rock band"));

        verify(artistService).listAll(any(Pageable.class));
    }

    @Test
    void shouldGetArtistById() throws Exception {
        UUID artistId = UUID.randomUUID();
        ArtistDetail detail = new ArtistDetail(
                artistId,
                "Daft Punk",
                Set.of("Electronic"),
                "French electronic music duo",
                List.of(),
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-02T00:00:00Z")
        );

        when(artistService.findById(artistId)).thenReturn(detail);

        mockMvc.perform(get("/artist/{id}", artistId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(artistId.toString()))
                .andExpect(jsonPath("$.name").value("Daft Punk"))
                .andExpect(jsonPath("$.genres[0]").value("Electronic"))
                .andExpect(jsonPath("$.bio").value("French electronic music duo"));

        verify(artistService).findById(artistId);
    }

    @Test
    void shouldRegisterArtist() throws Exception {
        UUID artistId = UUID.randomUUID();
        UUID genreId = UUID.randomUUID();
        RegisterArtistDto request = new RegisterArtistDto(
                "Nirvana",
                Set.of(genreId),
                "American rock band"
        );
        ArtistDetail detail = new ArtistDetail(
                artistId,
                "Nirvana",
                Set.of("Grunge"),
                "American rock band",
                List.of(),
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-02T00:00:00Z")
        );

        when(artistService.register(any(RegisterArtistDto.class))).thenReturn(detail);

        mockMvc.perform(post("/artist/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/artist/" + artistId)))
                .andExpect(jsonPath("$.id").value(artistId.toString()))
                .andExpect(jsonPath("$.name").value("Nirvana"))
                .andExpect(jsonPath("$.bio").value("American rock band"));

        verify(artistService).register(any(RegisterArtistDto.class));
    }

    @Test
    void shouldUpdateArtist() throws Exception {
        UUID artistId = UUID.randomUUID();
        UUID genreId = UUID.randomUUID();
        UpdateArtistDto request = new UpdateArtistDto(
                "Pink Floyd",
                Set.of(genreId),
                "Progressive rock band"
        );
        Artist updatedArtist = artist(artistId, "Pink Floyd", "Progressive rock band", "Progressive Rock");

        when(artistService.update(eq(artistId), any(UpdateArtistDto.class))).thenReturn(updatedArtist);

        mockMvc.perform(put("/artist/update/{id}", artistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(artistId.toString()))
                .andExpect(jsonPath("$.name").value("Pink Floyd"))
                .andExpect(jsonPath("$.genres[0]").value("Progressive Rock"))
                .andExpect(jsonPath("$.bio").value("Progressive rock band"));

        verify(artistService).update(eq(artistId), any(UpdateArtistDto.class));
    }

    @Test
    void shouldDeleteArtist() throws Exception {
        UUID artistId = UUID.randomUUID();
        doNothing().when(artistService).delete(artistId);

        mockMvc.perform(delete("/artist/{id}", artistId))
                .andExpect(status().isNoContent());

        verify(artistService).delete(artistId);
    }

    @Test
    void shouldUploadArtistImage() throws Exception {
        UUID artistId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "artist.png",
                MediaType.IMAGE_PNG_VALUE,
                "image-content".getBytes()
        );

        when(artistImageService.saveImageAsBase64(file, artistId)).thenReturn("data:image/png;base64,abc");

        mockMvc.perform(multipart("/artist/upload/imagem")
                        .file(file)
                        .param("artistId", artistId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("data:image/png;base64,abc"));

        verify(artistImageService).saveImageAsBase64(file, artistId);
    }

    @Test
    void shouldReturnInternalServerErrorWhenImageUploadFails() throws Exception {
        UUID artistId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "artist.png",
                MediaType.IMAGE_PNG_VALUE,
                "image-content".getBytes()
        );

        when(artistImageService.saveImageAsBase64(file, artistId)).thenThrow(new RuntimeException("invalid image"));

        mockMvc.perform(multipart("/artist/upload/imagem")
                        .file(file)
                        .param("artistId", artistId.toString()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erro ao salvar a imagem: invalid image"));

        verify(artistImageService).saveImageAsBase64(file, artistId);
    }

    private Artist artist(UUID artistId, String name, String bio, String genreName) {
        Genre genre = new Genre();
        genre.setId(UUID.randomUUID());
        genre.setName(genreName);

        Artist artist = new Artist();
        artist.setId(artistId);
        artist.setName(name);
        artist.setBio(bio);
        artist.setGenres(Set.of(genre));
        artist.setArtistImages(List.of());
        artist.setCreated_at(Instant.parse("2025-01-01T00:00:00Z"));
        artist.setUpdate_at(Instant.parse("2025-01-02T00:00:00Z"));
        return artist;
    }
}
