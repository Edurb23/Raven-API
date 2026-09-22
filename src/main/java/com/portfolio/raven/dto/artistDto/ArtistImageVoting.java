package com.portfolio.raven.dto.artistDto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ArtistImageVoting(LocalDate weekStart, Instant closesAt, String timezone,
                                UUID votedImageId, List<ImageVotes> images) {
    public record ImageVotes(UUID id, long votes, boolean selected) {}
}
