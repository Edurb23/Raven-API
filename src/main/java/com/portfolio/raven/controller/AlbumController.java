package com.portfolio.raven.controller;

import com.portfolio.raven.config.docs.ApiErrorResponse;
import com.portfolio.raven.dto.albumDto.AlbumDetail;
import com.portfolio.raven.dto.albumDto.RegisterAlbumDto;
import com.portfolio.raven.dto.albumDto.UpdateAlbumDto;
import com.portfolio.raven.service.AlbumService;
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
@RequestMapping("album")
@Tag(name = "Albums", description = "Endpoints for managing albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    @Operation(
            summary = "List albums",
            description = """
        Returns a paginated list of albums.

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
    public ResponseEntity<List<AlbumDetail>> getAll(
            @Parameter(description = "Page number (starts at 0)", example = "0")
            Pageable pageable
    ) {
        return ResponseEntity.ok(albumService.listAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get album by id", description = "Returns album details by UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<AlbumDetail> getById(
            @Parameter(description = "Album UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(albumService.findById(id));
    }

    @PostMapping("/register")
    @Transactional
    @Operation(summary = "Register a new album")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Register album example",
                            value = """
                                {
                                  "coverUrl": "https://cdn.example.com/covers/after-hours.jpg",
                                  "name": "After Hours",
                                  "artistId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                  "durationSeconds": 3372,
                                  "type": "LP",
                                  "releaseYear": 2020
                                }
                                """
                    )
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
    public ResponseEntity<AlbumDetail> post(
            @RequestBody @Valid RegisterAlbumDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        var albumDetail = albumService.register(dto);
        var uri = uriBuilder.path("/album/{id}").buildAndExpand(albumDetail.id()).toUri();
        return ResponseEntity.created(uri).body(albumDetail);
    }

    @PutMapping("/update/{id}")
    @Transactional
    @Operation(summary = "Update an album", description = "Updates an existing album by UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<AlbumDetail> update(
            @Parameter(description = "Album UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable UUID id,
            @RequestBody UpdateAlbumDto dto
    ) {
        return ResponseEntity.ok(albumService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Delete an album", description = "Deletes an album by UUID.")
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
            @Parameter(description = "Album UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable UUID id
    ) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
