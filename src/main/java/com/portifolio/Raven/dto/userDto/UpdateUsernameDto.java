package com.portifolio.Raven.dto.userDto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUsernameDto(
        @NotBlank
        String Newusername
) {
}
