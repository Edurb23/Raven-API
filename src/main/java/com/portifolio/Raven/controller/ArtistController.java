package com.portifolio.Raven.controller;

import com.portifolio.Raven.dto.artistDto.ArtistListDto;
import com.portifolio.Raven.dto.artistDto.RegisterArtistDto;
import com.portifolio.Raven.dto.artistDto.ArtistDetail;
import com.portifolio.Raven.dto.artistDto.UpdateArtistDto;
import com.portifolio.Raven.model.Artist;
import com.portifolio.Raven.repository.ArtistRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.data.domain.Pageable;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("artist")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;


    @GetMapping
    public ResponseEntity<List<ArtistListDto>>get(Pageable pageable){
        var ListArtist = artistRepository.findAll().stream().map(ArtistListDto::new).toList();
        return ok(ListArtist);
    }


    @PostMapping("register")
    @Transactional
    public ResponseEntity<ArtistDetail> post(@RequestBody @Valid RegisterArtistDto registerArtistDto, UriComponentsBuilder uriComponentsBuilder){
        var artistas = new Artist(registerArtistDto);
        artistRepository.save(artistas);
        var url = uriComponentsBuilder.path("/artist/{id}").buildAndExpand(artistas.getId()).toUri();
        return ResponseEntity.created(url).body(new ArtistDetail(artista));

    }


    @PutMapping("/upadate/{id}")
    @Transactional
    public ResponseEntity<ArtistDetail> put(@PathVariable("id") Long id, @RequestBody UpdateArtistDto dto){
        var artistas = artistRepository.getReferenceById(id);
        artistas.updateArtist(dto);
        return ResponseEntity.ok(new ArtistDetail(artistas));
    }


    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable("id")Long id){
        artistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
