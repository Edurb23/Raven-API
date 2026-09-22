package com.portfolio.raven.repository;

import com.portfolio.raven.dto.artistDto.ArtistImageVoting.ImageVotes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public class ArtistImageVoteRepository {
    private final JdbcTemplate jdbc;

    public ArtistImageVoteRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean artistExists(UUID artistId) {
        return Boolean.TRUE.equals(jdbc.queryForObject(
                "SELECT COUNT(*) > 0 FROM tb_raven_artists WHERE id = ? AND blocked = FALSE", Boolean.class, artistId.toString()));
    }

    // Serializes votes and elections for the same artist across server instances.
    public boolean lockArtist(UUID artistId) {
        return !jdbc.queryForList("SELECT id FROM tb_raven_artists WHERE id = ? AND blocked = FALSE FOR UPDATE",
                String.class, artistId.toString()).isEmpty();
    }

    public boolean imageBelongsToArtist(UUID artistId, UUID imageId) {
        return Boolean.TRUE.equals(jdbc.queryForObject(
                "SELECT COUNT(*) > 0 FROM tb_raven_artist_image WHERE id = ? AND artist_id = ?",
                Boolean.class, imageId.toString(), artistId.toString()));
    }

    public UUID userVote(UUID artistId, UUID userId, LocalDate week) {
        var ids = jdbc.queryForList("""
                SELECT image_id FROM tb_raven_artist_image_votes
                WHERE artist_id = ? AND user_id = ? AND week_start = ?
                """, String.class, artistId.toString(), userId.toString(), Date.valueOf(week));
        return ids.isEmpty() ? null : UUID.fromString(ids.get(0));
    }

    public void saveVote(UUID artistId, UUID userId, UUID imageId, LocalDate week, Instant now) {
        // The parent artist is already locked, so simultaneous first votes cannot race.
        if (userVote(artistId, userId, week) == null) {
            jdbc.update("""
                    INSERT INTO tb_raven_artist_image_votes (artist_id, user_id, week_start, image_id, updated_at)
                    VALUES (?, ?, ?, ?, ?)
                    """, artistId.toString(), userId.toString(), Date.valueOf(week), imageId.toString(), Timestamp.from(now));
        } else {
            jdbc.update("""
                    UPDATE tb_raven_artist_image_votes SET image_id = ?, updated_at = ?
                    WHERE artist_id = ? AND user_id = ? AND week_start = ?
                    """, imageId.toString(), Timestamp.from(now), artistId.toString(), userId.toString(), Date.valueOf(week));
        }
    }

    public List<ImageVotes> counts(UUID artistId, LocalDate week) {
        return jdbc.query("""
                SELECT i.id, i.selected, COUNT(v.user_id) AS votes
                FROM tb_raven_artist_image i LEFT JOIN tb_raven_artist_image_votes v
                  ON v.image_id = i.id AND v.artist_id = i.artist_id AND v.week_start = ?
                WHERE i.artist_id = ? GROUP BY i.id, i.selected, i.created_at
                ORDER BY i.selected DESC, i.created_at, i.id
                """, (rs, row) -> new ImageVotes(UUID.fromString(rs.getString("id")),
                rs.getLong("votes"), rs.getBoolean("selected")), Date.valueOf(week), artistId.toString());
    }

    public List<UUID> pendingArtists(LocalDate currentWeek) {
        return jdbc.query("""
                SELECT DISTINCT v.artist_id FROM tb_raven_artist_image_votes v
                JOIN tb_raven_artists a ON a.id = v.artist_id AND a.blocked = FALSE
                WHERE v.week_start < ? AND NOT EXISTS (
                  SELECT 1 FROM tb_raven_artist_image_elections e
                  WHERE e.artist_id = v.artist_id AND e.week_start = v.week_start)
                ORDER BY v.artist_id
                """, (rs, row) -> UUID.fromString(rs.getString(1)), Date.valueOf(currentWeek));
    }

    public List<LocalDate> pendingWeeks(UUID artistId, LocalDate currentWeek) {
        return jdbc.query("""
                SELECT DISTINCT v.week_start FROM tb_raven_artist_image_votes v
                WHERE v.artist_id = ? AND v.week_start < ? AND NOT EXISTS (
                  SELECT 1 FROM tb_raven_artist_image_elections e
                  WHERE e.artist_id = v.artist_id AND e.week_start = v.week_start)
                ORDER BY v.week_start
                """, (rs, row) -> rs.getDate(1).toLocalDate(), artistId.toString(), Date.valueOf(currentWeek));
    }

    public UUID winner(UUID artistId, LocalDate week) {
        var winners = jdbc.queryForList("""
                SELECT i.id FROM tb_raven_artist_image_votes v
                JOIN tb_raven_artist_image i ON i.id = v.image_id AND i.artist_id = v.artist_id
                WHERE v.artist_id = ? AND v.week_start = ?
                GROUP BY i.id, i.selected, i.created_at
                ORDER BY COUNT(*) DESC, i.selected DESC, i.created_at, i.id LIMIT 1
                """, String.class, artistId.toString(), Date.valueOf(week));
        return winners.isEmpty() ? null : UUID.fromString(winners.get(0));
    }

    public void recordWinner(UUID artistId, LocalDate week, UUID winner, Instant now) {
        if (winner != null) {
            jdbc.update("""
                    UPDATE tb_raven_artist_image SET selected = CASE WHEN id = ? THEN TRUE ELSE FALSE END,
                    update_at = ? WHERE artist_id = ?
                    """, winner.toString(), Timestamp.from(now), artistId.toString());
        }
        jdbc.update("""
                INSERT INTO tb_raven_artist_image_elections (artist_id, week_start, winner_image_id, processed_at)
                VALUES (?, ?, ?, ?)
                """, artistId.toString(), Date.valueOf(week), winner == null ? null : winner.toString(), Timestamp.from(now));
    }
}
