package com.portifolio.Raven.controller;

import com.portifolio.Raven.config.docs.ApiErrorResponse;
import com.portifolio.Raven.dto.musicDto.MusicDetail;
import com.portifolio.Raven.dto.musicDto.RegisterMusicDto;
import com.portifolio.Raven.dto.musicDto.UpdateMusicDto;
import com.portifolio.Raven.service.MusicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("music")
@Tag(name = "Music", description = "Endpoints for managing music tracks and singles")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @GetMapping
    @Operation(
            summary = "List music",
            description = """
        Returns a paginated list of music tracks and singles.

        Pagination parameters:
        - page: page number (starts at 0)
        - size: number of items per page
        - sort: field and direction (example: name,asc)
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<List<MusicDetail>> getAll(
            @Parameter(description = "Page number (starts at 0)", example = "0")
            Pageable pageable
    ) {
        return ResponseEntity.ok(musicService.listAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get music by id", description = "Returns music details by UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<MusicDetail> getById(
            @Parameter(description = "Music UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(musicService.findById(id));
    }

    @PostMapping("/register")
    @Transactional
    @Operation(summary = "Register a new music track or single")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Register album track",
                                    value = """
                                        {
                                          "name": "Blinding Lights",
                                          "artistId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                          "albumId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                          "durationSeconds": 200,
                                          "single": false,
                                          "trackNumber": 9
                                        }
                                        """
                            ),
                            @ExampleObject(
                                    name = "Register single",
                                    value = """
                                        {
                                          "name": "Standalone Single",
                                          "artistId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                          "albumId": null,
                                          "durationSeconds": 185,
                                          "single": true,
                                          "trackNumber": null
                                        }
                                        """
                            )
                    }
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<MusicDetail> post(
            @RequestBody @Valid RegisterMusicDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        var musicDetail = musicService.register(dto);
        var uri = uriBuilder.path("/music/{id}").buildAndExpand(musicDetail.id()).toUri();
        return ResponseEntity.created(uri).body(musicDetail);
    }

    @PutMapping("/update/{id}")
    @Transactional
    @Operation(summary = "Update music", description = "Updates an existing music track or single by UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<MusicDetail> update(
            @Parameter(description = "Music UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable UUID id,
            @RequestBody UpdateMusicDto dto
    ) {
        return ResponseEntity.ok(musicService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Delete music", description = "Deletes a music track or single by UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Music UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable UUID id
    ) {
        musicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
