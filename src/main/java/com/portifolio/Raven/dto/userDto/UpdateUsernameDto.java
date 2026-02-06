package com.portifolio.Raven.dto.userDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateUsernameDto(
        @NotBlank
        @Schema(description = "username", example = "Choose a username (not a real name)")
        String Newusername
) {
}
