package com.portifolio.Raven.controller;

import com.portifolio.Raven.dto.albumDto.AlbumDetail;
import com.portifolio.Raven.dto.artistDto.ArtistListDto;
import com.portifolio.Raven.dto.userDto.AlbumReviewDetail;
import com.portifolio.Raven.dto.userDto.AlbumReviewRequest;
import com.portifolio.Raven.dto.userDto.CreateAlbumListDto;
import com.portifolio.Raven.dto.userDto.UserAlbumListDetail;
import com.portifolio.Raven.service.UserLibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("user/{userId}")
@Tag(name = "User Library", description = "Endpoints for user album reviews, liked albums, lists and favorite artists")
public class UserLibraryController {

    @Autowired
    private UserLibraryService userLibraryService;

    @PostMapping("/album-reviews/{albumId}")
    @Transactional
    @Operation(summary = "Rate and comment an album", description = "Creates or updates a user album review with a rating from 0 to 5.")
    public ResponseEntity<AlbumReviewDetail> reviewAlbum(
            @PathVariable UUID userId,
            @PathVariable UUID albumId,
            @RequestBody @Valid AlbumReviewRequest dto
    ) {
        return ResponseEntity.ok(userLibraryService.reviewAlbum(userId, albumId, dto));
    }

    @GetMapping("/album-reviews")
    @Operation(summary = "List user album reviews")
    public ResponseEntity<List<AlbumReviewDetail>> listReviews(@PathVariable UUID userId) {
        return ResponseEntity.ok(userLibraryService.listReviews(userId));
    }

    @PostMapping("/liked-albums/{albumId}")
    @Transactional
    @Operation(summary = "Like an album", description = "Adds the album to the user's liked albums.")
    public ResponseEntity<Void> likeAlbum(@PathVariable UUID userId, @PathVariable UUID albumId) {
        userLibraryService.likeAlbum(userId, albumId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/liked-albums/{albumId}")
    @Transactional
    @Operation(summary = "Unlike an album")
    public ResponseEntity<Void> unlikeAlbum(@PathVariable UUID userId, @PathVariable UUID albumId) {
        userLibraryService.unlikeAlbum(userId, albumId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/liked-albums")
    @Operation(summary = "List liked albums")
    public ResponseEntity<List<AlbumDetail>> listLikedAlbums(@PathVariable UUID userId) {
        return ResponseEntity.ok(userLibraryService.listLikedAlbums(userId));
    }

    @PostMapping("/album-lists")
    @Transactional
    @Operation(summary = "Create a custom album list")
    public ResponseEntity<UserAlbumListDetail> createAlbumList(
            @PathVariable UUID userId,
            @RequestBody @Valid CreateAlbumListDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        UserAlbumListDetail detail = userLibraryService.createAlbumList(userId, dto);
        var uri = uriBuilder.path("/user/{userId}/album-lists/{listId}")
                .buildAndExpand(userId, detail.id())
                .toUri();
        return ResponseEntity.created(uri).body(detail);
    }

    @GetMapping("/album-lists")
    @Operation(summary = "List custom album lists")
    public ResponseEntity<List<UserAlbumListDetail>> listAlbumLists(@PathVariable UUID userId) {
        return ResponseEntity.ok(userLibraryService.listAlbumLists(userId));
    }

    @DeleteMapping("/album-lists/{listId}")
    @Transactional
    @Operation(summary = "Delete a custom album list")
    public ResponseEntity<Void> deleteAlbumList(
            @PathVariable UUID userId,
            @PathVariable UUID listId
    ) {
        userLibraryService.deleteAlbumList(userId, listId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/album-lists/{listId}/albums/{albumId}")
    @Transactional
    @Operation(summary = "Add an album to a custom list")
    public ResponseEntity<UserAlbumListDetail> addAlbumToList(
            @PathVariable UUID userId,
            @PathVariable UUID listId,
            @PathVariable UUID albumId
    ) {
        return ResponseEntity.ok(userLibraryService.addAlbumToList(userId, listId, albumId));
    }

    @DeleteMapping("/album-lists/{listId}/albums/{albumId}")
    @Transactional
    @Operation(summary = "Remove an album from a custom list")
    public ResponseEntity<Void> removeAlbumFromList(
            @PathVariable UUID userId,
            @PathVariable UUID listId,
            @PathVariable UUID albumId
    ) {
        userLibraryService.removeAlbumFromList(userId, listId, albumId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/favorite-artists/{artistId}")
    @Transactional
    @Operation(summary = "Choose a favorite artist")
    public ResponseEntity<Void> favoriteArtist(@PathVariable UUID userId, @PathVariable UUID artistId) {
        userLibraryService.favoriteArtist(userId, artistId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/favorite-artists/{artistId}")
    @Transactional
    @Operation(summary = "Remove a favorite artist")
    public ResponseEntity<Void> unfavoriteArtist(@PathVariable UUID userId, @PathVariable UUID artistId) {
        userLibraryService.unfavoriteArtist(userId, artistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorite-artists")
    @Operation(summary = "List favorite artists")
    public ResponseEntity<List<ArtistListDto>> listFavoriteArtists(@PathVariable UUID userId) {
        return ResponseEntity.ok(userLibraryService.listFavoriteArtists(userId));
    }
}
