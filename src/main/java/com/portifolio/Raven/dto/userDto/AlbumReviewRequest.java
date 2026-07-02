package com.portifolio.Raven.dto.userDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AlbumReviewRequest(
        @NotNull @Min(0) @Max(5) Integer rating,
        @Size(max = 1000) String comment
) {
}
