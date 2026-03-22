package com.portfolio.raven.controller;

import com.portfolio.raven.config.docs.ApiErrorResponse;
import com.portfolio.raven.dto.userDto.*;
import com.portfolio.raven.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("user")
@Tag(name = "Users", description = "Endpoints for managing users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    @Operation(
            summary = "List users",
            description = """
        Returns a paginated list of users.

        Pagination parameters:
        - page: page number (starts at 0)
        - size: number of items per page
        - sort: field and direction (example: username,asc)
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
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<UserList>> listAll(
            @Parameter(description = "Page number (starts at 0)", example = "0")
            Pageable pageable
    ){
        var ListUser = userService.listAll(pageable).stream().toList();
        return ok(ListUser);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Get user by id",
            description = "Returns user details by UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserDetail> getByID(
            @Parameter(description = "User UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable("id") UUID id
    ){
        var userDetail = userService.getById(id);
        return ok(userDetail);
    }

    @PostMapping("/register")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user and returns its details. The Location header points to /user/{id}."
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
    public ResponseEntity<UserDetail> post(
            @RequestBody @Valid RegisterUserDto dto,
            UriComponentsBuilder builder
    ){
        UserDetail userDetail = userService.create(dto);
        var uri = builder.path("/user/{id}").buildAndExpand(userDetail.id()).toUri();
        return ResponseEntity.created(uri).body(userDetail);
    }

    @PutMapping("/{id}/email")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Update user email",
            description = "Updates the email of a user by UUID."
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
    public ResponseEntity<UserDetail> updateEmail(
            @Parameter(description = "User UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable("id") UUID id,

            @RequestBody @Valid UpdateEmailDto dto
    ){
        UserDetail updated = userService.updateEmail(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/username")
    @Operation(
            summary = "Update username",
            description = "Updates the username of a user by UUID."
    )
    @SecurityRequirement(name = "bearerAuth")
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
    public ResponseEntity<UserDetail> updateUsername(
            @Parameter(description = "User UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable("id") UUID id,
            @RequestBody @Valid UpdateUsernameDto dto
    ){
        UserDetail updated = userService.updateUsername(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete user",
            description = "Deletes a user by UUID."
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
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
            @PathVariable("id") UUID id
    ){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
