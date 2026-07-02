package com.portfolio.raven.controller;

import com.portfolio.raven.service.AlbumCoverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("album/{albumId}/covers")
@RequiredArgsConstructor
@Tag(name = "Album Covers", description = "Endpoints for managing album covers")
public class AlbumCoverController {

    private final AlbumCoverService albumCoverService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload album cover")
    public ResponseEntity<String> uploadCover(
            @PathVariable UUID albumId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            return ResponseEntity.ok(albumCoverService.saveCoverAsBase64(file, albumId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao salvar a capa: " + e.getMessage());
        }
    }

    @PutMapping("/{coverId}/select")
    @Operation(summary = "Select album main cover")
    public ResponseEntity<String> selectCover(
            @PathVariable UUID albumId,
            @PathVariable UUID coverId
    ) {
        return ResponseEntity.ok(albumCoverService.selectAlbumCover(albumId, coverId));
    }
}
