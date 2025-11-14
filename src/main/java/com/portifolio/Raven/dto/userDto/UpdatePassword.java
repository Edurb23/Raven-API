package com.portifolio.Raven.dto.userDto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePassword(
        @NotBlank
        String password,
        @NotBlank
        String newPassoword
) {
}
