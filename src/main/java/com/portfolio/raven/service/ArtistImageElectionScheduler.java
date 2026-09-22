package com.portfolio.raven.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@ConditionalOnProperty(name = "raven.image-voting.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class ArtistImageElectionScheduler {
    private static final Logger log = LoggerFactory.getLogger(ArtistImageElectionScheduler.class);
    private final ArtistImageVotingService voting;

    public ArtistImageElectionScheduler(ArtistImageVotingService voting) {
        this.voting = voting;
    }

    // Also recovers weeks missed while the application was offline.
    @Scheduled(initialDelay = 10000, fixedDelay = 60000)
    public void closeCompletedWeeks() {
        for (var artistId : voting.pendingArtists()) {
            try {
                voting.closeCompletedWeeks(artistId);
            } catch (RuntimeException error) {
                log.error("Could not complete weekly image election for artist {}", artistId, error);
            }
        }
    }
}
