package com.portifolio.Raven.dto.userDto;

import jakarta.validation.constraints.NotBlank;

public record RegisterUserDto(

        @NotBlank
        String username,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        Boolean status






) {
}
