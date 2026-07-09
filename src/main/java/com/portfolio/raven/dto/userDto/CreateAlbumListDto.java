package com.portfolio.raven.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAlbumListDto(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 500) String description
) {
}
