package com.portifolio.Raven.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenDto(


        @Schema(
                description = "JWT access token",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        String token

) {
}
