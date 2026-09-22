package com.portfolio.raven.controller;

import com.portfolio.raven.dto.artistDto.ArtistImageVoting;
import com.portfolio.raven.entity.User;
import com.portfolio.raven.service.ArtistImageVotingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/artist/{artistId}/images")
public class ArtistImageVotingController {
    private final ArtistImageVotingService voting;

    public ArtistImageVotingController(ArtistImageVotingService voting) {
        this.voting = voting;
    }

    @GetMapping("/votes")
    public ArtistImageVoting getVoting(@PathVariable UUID artistId, @AuthenticationPrincipal User user) {
        return voting.getVoting(artistId, user.getId());
    }

    @PutMapping("/{imageId}/vote")
    public ArtistImageVoting vote(@PathVariable UUID artistId, @PathVariable UUID imageId,
                                 @AuthenticationPrincipal User user) {
        return voting.vote(artistId, imageId, user.getId());
    }
}
