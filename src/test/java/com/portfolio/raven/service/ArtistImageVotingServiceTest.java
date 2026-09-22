package com.portfolio.raven.service;

import com.portfolio.raven.repository.ArtistImageVoteRepository;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.time.*;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(ArtistImageVotingServiceTest.Config.class)
class ArtistImageVotingServiceTest {
    @Autowired ArtistImageVotingService service;
    @Autowired JdbcTemplate jdbc;
    @Autowired MutableClock clock;
    private UUID artist, otherArtist, first, second, foreignImage, user, otherUser;

    @BeforeEach
    void seed() {
        jdbc.update("DELETE FROM tb_raven_artist_image_elections");
        jdbc.update("DELETE FROM tb_raven_artist_image_votes");
        jdbc.update("DELETE FROM tb_raven_artist_image");
        jdbc.update("DELETE FROM tb_raven_users");
        jdbc.update("DELETE FROM tb_raven_artists");
        clock.now = Instant.parse("2026-09-22T12:00:00Z");
        artist = UUID.randomUUID(); otherArtist = UUID.randomUUID();
        first = UUID.randomUUID(); second = UUID.randomUUID(); foreignImage = UUID.randomUUID();
        user = UUID.randomUUID(); otherUser = UUID.randomUUID();
        for (var id : new UUID[]{artist, otherArtist}) jdbc.update("INSERT INTO tb_raven_artists(id) VALUES (?)", id.toString());
        for (var id : new UUID[]{user, otherUser}) jdbc.update("INSERT INTO tb_raven_users(id) VALUES (?)", id.toString());
        image(first, artist, true, "2026-01-01 00:00:00");
        image(second, artist, false, "2026-01-02 00:00:00");
        image(foreignImage, otherArtist, true, "2026-01-01 00:00:00");
    }

    void image(UUID id, UUID artistId, boolean selected, String created) {
        jdbc.update("INSERT INTO tb_raven_artist_image(id, artist_id, selected, created_at) VALUES (?, ?, ?, ?)",
                id.toString(), artistId.toString(), selected, created);
    }

    long count(UUID id) {
        return service.getVoting(artist, user).images().stream().filter(image -> image.id().equals(id)).findFirst().orElseThrow().votes();
    }

    UUID selected() {
        return service.getVoting(artist, user).images().stream().filter(image -> image.selected()).findFirst().orElseThrow().id();
    }

    @Test
    void repeatedVotesAndChangesCountOnlyOncePerUserAndArtist() {
        service.vote(artist, first, user);
        service.vote(artist, first, user);
        assertEquals(1, count(first));
        service.vote(artist, second, user);
        service.vote(artist, first, otherUser);
        service.vote(otherArtist, foreignImage, user);
        assertEquals(1, count(first));
        assertEquals(1, count(second));
        assertEquals(second, service.getVoting(artist, user).votedImageId());
        assertEquals(first, selected());
    }

    @Test
    void rejectsImagesFromAnotherArtistAndUnknownArtists() {
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.vote(artist, foreignImage, user)).getStatusCode().value());
        assertThrows(ResponseStatusException.class, () -> service.getVoting(UUID.randomUUID(), user));
        assertEquals(0, count(first));
    }

    @Test
    void closesAtMondayInSaoPauloAndStartsNewVotingWeek() {
        service.vote(artist, second, user);
        assertEquals(Instant.parse("2026-09-28T03:00:00Z"), service.getVoting(artist, user).closesAt());
        clock.now = Instant.parse("2026-09-28T02:59:59Z");
        service.closeCompletedWeeks(artist);
        assertEquals(first, selected());
        clock.now = Instant.parse("2026-09-28T03:00:00Z");
        assertNull(service.getVoting(artist, user).votedImageId());
        service.vote(artist, first, user);
        service.closeCompletedWeeks(artist);
        assertEquals(second, selected());
        assertEquals(1, count(first));
        assertEquals(0, count(second));
    }

    @Test
    void tiesKeepCurrentPhotoAndNoVotesDoNotChangeIt() {
        service.vote(artist, first, user);
        service.vote(artist, second, otherUser);
        clock.now = Instant.parse("2026-09-28T03:00:00Z");
        service.closeCompletedWeeks(artist);
        assertEquals(first, selected());
        clock.now = Instant.parse("2026-10-05T03:00:00Z");
        service.closeCompletedWeeks(artist);
        assertEquals(first, selected());
    }

    @Test
    void tiedCandidatesWithoutCurrentPhotoUseOldestUpload() {
        var third = UUID.randomUUID();
        image(third, artist, false, "2026-01-03 00:00:00");
        service.vote(artist, second, user);
        service.vote(artist, third, otherUser);
        clock.now = Instant.parse("2026-09-28T03:00:00Z");
        service.closeCompletedWeeks(artist);
        assertEquals(second, selected());
    }

    @Test
    void catchesUpMissedWeeksInOrderAndDoesNotReapplyThem() {
        service.vote(artist, second, user);
        clock.now = Instant.parse("2026-09-29T12:00:00Z");
        service.vote(artist, first, user);
        clock.now = Instant.parse("2026-10-06T12:00:00Z");
        assertTrue(service.pendingArtists().contains(artist));
        service.closeCompletedWeeks(artist);
        assertEquals(first, selected());
        assertTrue(service.pendingArtists().isEmpty());
        service.closeCompletedWeeks(artist);
        assertEquals(2, jdbc.queryForObject("SELECT COUNT(*) FROM tb_raven_artist_image_elections", Integer.class));
    }

    @Test
    void concurrentVotesAndElectionsAreIdempotent() throws Exception {
        var executor = Executors.newFixedThreadPool(2);
        try {
            var a = executor.submit(() -> service.vote(artist, first, user));
            var b = executor.submit(() -> service.vote(artist, second, user));
            a.get(10, TimeUnit.SECONDS); b.get(10, TimeUnit.SECONDS);
            assertEquals(1, count(first) + count(second));
            clock.now = Instant.parse("2026-09-28T03:00:00Z");
            var c = executor.submit(() -> service.closeCompletedWeeks(artist));
            var d = executor.submit(() -> service.closeCompletedWeeks(artist));
            c.get(10, TimeUnit.SECONDS); d.get(10, TimeUnit.SECONDS);
            assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM tb_raven_artist_image_elections", Integer.class));
        } finally {
            executor.shutdownNow();
        }
    }

    @Configuration
    @EnableTransactionManagement
    static class Config {
        @Bean DataSource dataSource() {
            var source = new DriverManagerDataSource("jdbc:h2:mem:imagevotes;MODE=MySQL;DB_CLOSE_DELAY=-1", "sa", "");
            var db = new JdbcTemplate(source);
            db.execute("CREATE TABLE tb_raven_artists(id VARCHAR(36) PRIMARY KEY, blocked BOOLEAN DEFAULT FALSE NOT NULL)");
            db.execute("CREATE TABLE tb_raven_users(id VARCHAR(36) PRIMARY KEY)");
            db.execute("CREATE TABLE tb_raven_artist_image(id VARCHAR(36) PRIMARY KEY, artist_id VARCHAR(36), selected BOOLEAN, created_at TIMESTAMP, update_at TIMESTAMP)");
            return source;
        }
        @Bean SpringLiquibase liquibase(DataSource source) {
            var migration = new SpringLiquibase();
            migration.setDataSource(source);
            migration.setChangeLog("classpath:db/changelog/008-weekly-artist-image-votes.sql");
            return migration;
        }
        @Bean JdbcTemplate jdbc(DataSource source) { return new JdbcTemplate(source); }
        @Bean DataSourceTransactionManager transactionManager(DataSource source) { return new DataSourceTransactionManager(source); }
        @Bean MutableClock clock() { return new MutableClock(); }
        @Bean ArtistImageVoteRepository repository(JdbcTemplate jdbc) { return new ArtistImageVoteRepository(jdbc); }
        @Bean ArtistImageVotingService service(ArtistImageVoteRepository repository, MutableClock clock) {
            return new ArtistImageVotingService(repository, clock, "America/Sao_Paulo");
        }
    }

    static class MutableClock extends Clock {
        volatile Instant now = Instant.parse("2026-09-22T12:00:00Z");
        public ZoneId getZone() { return ZoneOffset.UTC; }
        public Clock withZone(ZoneId zone) { return Clock.fixed(now, zone); }
        public Instant instant() { return now; }
    }
}
