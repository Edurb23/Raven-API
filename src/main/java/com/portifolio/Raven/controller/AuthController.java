package com.portifolio.Raven.controller;

import com.portifolio.Raven.dto.token.AuthDto;
import com.portifolio.Raven.dto.token.TokenDto;
import com.portifolio.Raven.service.AuthService;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
@Tag(name = "Authentication", description = "Endpoints for authentication and token generation")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Register artist example",
                                    value = """
                                            {
                                                "email": "user@gmail.com",
                                                "password": "user2025"
                                            }
                                """
                            )
                    }
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid payload", content = @Content)
    })
    public ResponseEntity<TokenDto> login(
            @RequestBody AuthDto dto
    ){
        var response = authService.auth(dto);
        return ResponseEntity.ok(response);
    }
}
