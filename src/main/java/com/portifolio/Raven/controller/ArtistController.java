package com.portifolio.Raven.controller;

import com.portifolio.Raven.dto.artistDto.*;
import com.portifolio.Raven.repository.ArtistRepository;
import com.portifolio.Raven.service.ArtistImageService;
import com.portifolio.Raven.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("artist")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistImageService artistImageService;

    @Autowired
    private ArtistService artistService;

    @GetMapping
    public ResponseEntity<List<ArtistListDto>> getAll(Pageable pageable) {
        var artists = artistService.listAll(pageable);
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDetail> getById(@PathVariable UUID id) {
        ArtistDetail detail = artistService.findById(id);
        System.out.println(detail.generos());
        return ResponseEntity.ok(detail);
    }


    @PostMapping("/register")
    @Transactional
    public ResponseEntity<ArtistDetail> post(@RequestBody @Valid RegisterArtistDto dto, UriComponentsBuilder uriBuilder) {
        var artistDetail = artistService.register(dto);
        var uri = uriBuilder.path("/artist/{id}").buildAndExpand(artistDetail.id()).toUri();
        return ResponseEntity.created(uri).body(artistDetail);
    }


    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<ArtistDetail> update(@PathVariable UUID id, @RequestBody UpdateArtistDto dto) {
        var artist = artistService.update(id, dto);
        var response = new ArtistDetail(artist);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        artistService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Upload de imagem

    @PostMapping("/upload/imagem")
    public ResponseEntity<String> uploadImage(@ModelAttribute ArtistImagemDto dto) {
        try {
            String imageUrl = artistImageService.saveImageAsBase64(dto.file(), dto.artistId());
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao salvar a imagem: " + e.getMessage());
        }
    }
}
