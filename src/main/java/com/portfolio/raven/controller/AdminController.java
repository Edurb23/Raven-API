package com.portfolio.raven.controller;

import com.portfolio.raven.dto.artistDto.*;
import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.mappers.ArtistMapper;
import com.portfolio.raven.repository.*;
import com.portfolio.raven.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminControlService controls;
    private final ArtistRepository artists;
    private final GenresRepository genres;
    private final ArtistService service;
    private final ArtistMapper mapper;
    private final ArtistImageService images;
    public AdminController(AdminControlService controls, ArtistRepository artists, GenresRepository genres,
                           ArtistService service, ArtistMapper mapper, ArtistImageService images) {
        this.controls=controls; this.artists=artists; this.genres=genres; this.service=service; this.mapper=mapper; this.images=images;
    }
    public record Toggle(@NotNull Boolean enabled) {}
    public record Block(@NotNull Boolean blocked) {}
    public record GenreOption(UUID id, String name) {}
    public record ArtistSummary(UUID id, String name, boolean blocked) {}
    public record ArtistEdit(ArtistDetail artist, boolean blocked, Set<UUID> genreIds) {}

    @GetMapping("/flags") public List<AdminControlService.Flag> flags() { return controls.flags(); }
    @PutMapping("/flags/{key}") public List<AdminControlService.Flag> flag(@PathVariable String key, @Valid @RequestBody Toggle dto) { return controls.setFlag(key, dto.enabled()); }
    @GetMapping("/logs") public List<AdminControlService.Log> logs(@RequestParam(defaultValue="0") int page,
                                                                @RequestParam(defaultValue="false") boolean errorsOnly) { return controls.logs(page, errorsOnly); }
    @GetMapping("/genres") public List<GenreOption> genres() { return genres.findAll(Sort.by("name")).stream().map(g -> new GenreOption(g.getId(),g.getName())).toList(); }
    @GetMapping("/artists") @Transactional(readOnly=true)
    public List<ArtistSummary> artists(@RequestParam(defaultValue="0") int page) {
        return artists.findAll(PageRequest.of(Math.max(0,page), 30, Sort.by("name"))).stream()
                .map(a -> new ArtistSummary(a.getId(),a.getName(),a.isBlocked())).toList();
    }
    private Artist required(UUID id) { return artists.findWithGenres(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Artist not found")); }
    private ArtistEdit edit(Artist artist) {
        return new ArtistEdit(mapper.toDetailDto(artist),artist.isBlocked(),artist.getGenres().stream().map(g -> g.getId()).collect(java.util.stream.Collectors.toSet()));
    }
    @GetMapping("/artists/{id}") @Transactional(readOnly=true)
    public ArtistEdit artist(@PathVariable UUID id) { return edit(required(id)); }
    @PostMapping("/artists") @ResponseStatus(HttpStatus.CREATED) @Transactional
    public ArtistEdit create(@Valid @RequestBody RegisterArtistDto dto) { return edit(required(service.register(dto).id())); }
    @PutMapping("/artists/{id}") @Transactional
    public ArtistEdit update(@PathVariable UUID id, @Valid @RequestBody RegisterArtistDto dto) {
        return edit(service.update(id,new UpdateArtistDto(dto.name(),dto.genres(),dto.bio())));
    }
    @PatchMapping("/artists/{id}/blocked") @Transactional
    public ArtistEdit block(@PathVariable UUID id, @Valid @RequestBody Block dto) {
        var artist=required(id); artist.setBlocked(dto.blocked()); return edit(artist);
    }
    @PostMapping(value="/artists/{id}/images", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String,String> upload(@PathVariable UUID id, @RequestParam MultipartFile file) {
        controls.requireEnabled("artist_photo_uploads");
        return Map.of("message",images.saveImageAsBase64(file,id));
    }
    @PutMapping("/artists/{id}/images/{imageId}/select")
    public Map<String,String> select(@PathVariable UUID id, @PathVariable UUID imageId) {
        return Map.of("message",images.selectArtistImage(id,imageId));
    }

    @DeleteMapping("/artists/{id}/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePhoto(@PathVariable UUID id, @PathVariable UUID imageId) {
        images.removeImage(id, imageId);
    }

    @PostMapping(value="/artists/{id}/banner", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String,String> uploadBanner(@PathVariable UUID id, @RequestParam MultipartFile file) {
        controls.requireEnabled("artist_photo_uploads");
        images.saveBanner(file, id);
        return Map.of("message", "Artist background updated");
    }

    @DeleteMapping("/artists/{id}/banner")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBanner(@PathVariable UUID id) { images.removeBanner(id); }
}
