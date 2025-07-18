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

import static org.springframework.http.ResponseEntity.ok;

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
    public ResponseEntity<List<ArtistListDto>> get(Pageable pageable) {
        var listArtist = artistRepository.findAll().stream().map(ArtistListDto::new).toList();
        return ok(listArtist);
    }

    @GetMapping("{id}")
    public ResponseEntity<ArtistDetail> getByid(@PathVariable("id") UUID id) {
        var artist = artistRepository.getReferenceById(id);
        return ok(new ArtistDetail(artist));
    }

  

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<ArtistDetail> post(@RequestBody @Valid RegisterArtistDto dto, UriComponentsBuilder uriBuilder) {
        var artistDetail = artistService.register(dto);
        var uri = uriBuilder.path("/artist/{id}").buildAndExpand(artistDetail.id()).toUri();
        return ResponseEntity.created(uri).body(artistDetail);
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

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<ArtistDetail> put(@PathVariable("id") UUID id, @RequestBody UpdateArtistDto dto) {
        var artist = artistRepository.getReferenceById(id);
        artist.updateArtist(dto);
        return ResponseEntity.ok(new ArtistDetail(artist));
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        artistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
