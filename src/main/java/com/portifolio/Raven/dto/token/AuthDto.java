package com.portifolio.Raven.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthDto(
        @NotBlank
        @Schema(
                description = "User email used to authenticate",
                example = "user@email.com"
        )
        String email,


        @NotBlank
        @Schema(
                description = "User password",
                example = "P@ssw0rd123"
        )
        String password

) {
}
