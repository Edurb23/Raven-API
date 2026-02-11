package com.portfolio.raven.controller;

import com.portfolio.raven.config.docs.ApiErrorResponse;
import com.portfolio.raven.dto.generoDto.GeneroList;
import com.portfolio.raven.dto.generoDto.GenreDetail;
import com.portfolio.raven.dto.generoDto.RegisterGenero;
import com.portfolio.raven.service.GenresService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("genre")
@Tag(name = "Genres", description = "Endpoints for managing genres")
public class GenresController {

    @Autowired
    private GenresService genresService;

    @GetMapping("/list")
    @Operation(
            summary = "List genre",
            description = """
        Returns a paginated list of genere.

        Pagination parameters:
        - page: page number (starts at 0)
        - size: number of items per page
        - sort: field and direction (example: name)
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
    public ResponseEntity<List<GeneroList>> listALl(
            @Parameter(description = "Page number (starts at 0)", example = "0")
            Pageable pageable
    ){
        var ListGenres = genresService.listAll(pageable).stream().toList();
        return ok(ListGenres);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Get genre by id",
            description = "Returns genre details by UUID."
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
    public ResponseEntity<GenreDetail> getByID(
            @Parameter(description = "Genre UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable("id") UUID id
    ) {
        var genreDetail = genresService.getById(id);
        return ok(genreDetail);
    }

    @PostMapping("/register")
    @Transactional
    @Operation(
            summary = "Register a new genre",
            description = "Creates a new genre and returns its details. The Location header points to /genero/{id}."
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
    public ResponseEntity<GenreDetail> post(
            @RequestBody @Valid RegisterGenero dto,
            UriComponentsBuilder uriComponentsBuilder
    ){
        var genreDetail = genresService.register(dto);
        var uri = uriComponentsBuilder.path("/genero/{id}").buildAndExpand(genreDetail.id()).toUri();
        return ResponseEntity.created(uri).body(genreDetail);
    }
}
