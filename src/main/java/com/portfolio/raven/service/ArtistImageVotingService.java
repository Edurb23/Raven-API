package com.portfolio.raven.service;

import com.portfolio.raven.dto.artistDto.ArtistImageVoting;
import com.portfolio.raven.repository.ArtistImageVoteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

@Service
public class ArtistImageVotingService {
    private final ArtistImageVoteRepository repository;
    private final Clock clock;
    private final ZoneId zone;

    public ArtistImageVotingService(ArtistImageVoteRepository repository, Clock clock,
            @Value("${raven.image-voting.timezone:America/Sao_Paulo}") String timezone) {
        this.repository = repository;
        this.clock = clock;
        this.zone = ZoneId.of(timezone);
    }

    public LocalDate currentWeek() {
        return LocalDate.now(clock.withZone(zone)).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    @Transactional(readOnly = true)
    public ArtistImageVoting getVoting(UUID artistId, UUID userId) {
        if (!repository.artistExists(artistId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
        }
        return snapshot(artistId, userId, currentWeek());
    }

    @Transactional
    public ArtistImageVoting vote(UUID artistId, UUID imageId, UUID userId) {
        if (!repository.lockArtist(artistId) || !repository.imageBelongsToArtist(artistId, imageId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist image not found");
        }
        LocalDate week = currentWeek();
        repository.saveVote(artistId, userId, imageId, week, clock.instant());
        return snapshot(artistId, userId, week);
    }

    private ArtistImageVoting snapshot(UUID artistId, UUID userId, LocalDate week) {
        return new ArtistImageVoting(week, week.plusWeeks(1).atStartOfDay(zone).toInstant(), zone.getId(),
                repository.userVote(artistId, userId, week), repository.counts(artistId, week));
    }

    @Transactional(readOnly = true)
    public List<UUID> pendingArtists() {
        return repository.pendingArtists(currentWeek());
    }

    @Transactional
    public void closeCompletedWeeks(UUID artistId) {
        if (!repository.lockArtist(artistId)) return;
        for (LocalDate week : repository.pendingWeeks(artistId, currentWeek())) {
            repository.recordWinner(artistId, week, repository.winner(artistId, week), clock.instant());
        }
    }
}
