package com.portifolio.Raven.dto.userDto;

import jakarta.validation.constraints.NotBlank;

public record UpdateEmailDto(
        @NotBlank
        String newemail,
        @NotBlank
        String password


) {
}
