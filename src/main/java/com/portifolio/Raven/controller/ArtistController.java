package com.portifolio.Raven.controller;

import com.portifolio.Raven.config.docs.ApiErrorResponse;
import com.portifolio.Raven.dto.artistDto.*;
import com.portifolio.Raven.repository.ArtistRepository;
import com.portifolio.Raven.service.ArtistImageService;
import com.portifolio.Raven.service.ArtistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("artist")
@Tag(name = "Artists", description = "Endpoints for managing artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistImageService artistImageService;

    @Autowired
    private ArtistService artistService;

    @GetMapping
    @Operation(
            summary = "List artist",
            description = """
        Returns a paginated list of artist.

        Pagination parameters:
        - page: page number (starts at 0)
        - size: number of items per page
        - sort: field and direction (example: nameArtist)
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<List<ArtistListDto>> getAll(
            @Parameter(description = "Page number (starts at 0)", example = "0")
            Pageable pageable
    ) {
        var artists = artistService.listAll(pageable);
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get artist by id",
            description = "Returns artist details by UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ArtistDetail> getById(
            @Parameter(description = "Artist UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable UUID id
    ) {
        ArtistDetail detail = artistService.findById(id);
        System.out.println(detail.generos());
        return ResponseEntity.ok(detail);
    }

    @PostMapping("/register")
    @Transactional
    @Operation(summary = "Register a new artist")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Register artist example",
                                    value = """
                                {
                                  "nomeArtist": "ROCK BAND",
                                 "generos": ["ff300b48-c11b-11f0-9159-82bc3ccdfda3"],
                                  "biografia": "ROCK BAND IS ..."
                                
                                }
                                """
                            )
                    }
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ArtistDetail> post(
            @RequestBody @Valid RegisterArtistDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        var artistDetail = artistService.register(dto);
        var uri = uriBuilder.path("/artist/{id}").buildAndExpand(artistDetail.id()).toUri();
        return ResponseEntity.created(uri).body(artistDetail);
    }

    @PutMapping("/update/{id}")
    @Transactional
    @Operation(
            summary = "Update an artist",
            description = "Updates an existing artist by UUID and returns the updated details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<ArtistDetail> update(
            @Parameter(description = "Artist UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable UUID id,
            @RequestBody UpdateArtistDto dto
    ) {
        var artist = artistService.update(id, dto);
        var response = new ArtistDetail(artist);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Delete an artist",
            description = "Deletes an artist by UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Artist UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable UUID id
    ) {
        artistService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/upload/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload artist image",
            description = "Uploads an image for an artist using multipart/form-data (ModelAttribute). Returns the image URL/base64 reference."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<String> uploadImage(
            @Parameter(description = "Form-data with file and artistId")
            @ModelAttribute ArtistImagemDto dto
    ) {
        try {
            String imageUrl = artistImageService.saveImageAsBase64(dto.file(), dto.artistId());
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao salvar a imagem: " + e.getMessage());
        }
    }
}
