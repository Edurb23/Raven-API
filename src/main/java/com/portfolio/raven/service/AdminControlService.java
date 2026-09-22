package com.portfolio.raven.service;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AdminControlService {
    private final JdbcTemplate jdbc;
    public AdminControlService(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public record Flag(String key, String description, boolean enabled, Instant updatedAt) {}
    public record Log(String id, Instant occurredAt, String actorId, String method, String path, int status, long durationMs) {}

    public List<Flag> flags() {
        return jdbc.query("SELECT * FROM tb_raven_feature_flags ORDER BY flag_key",
                (r, n) -> new Flag(r.getString("flag_key"), r.getString("description"), r.getBoolean("enabled"), r.getTimestamp("updated_at").toInstant()));
    }
    public boolean enabled(String key) {
        return Boolean.TRUE.equals(jdbc.queryForObject("SELECT enabled FROM tb_raven_feature_flags WHERE flag_key = ?", Boolean.class, key));
    }
    public void requireEnabled(String key) {
        if (!enabled(key)) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "This feature is temporarily unavailable");
    }
    @Transactional
    public List<Flag> setFlag(String key, boolean enabled) {
        if (jdbc.update("UPDATE tb_raven_feature_flags SET enabled=?, updated_at=? WHERE flag_key=?",
                enabled, Timestamp.from(Instant.now()), key) == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Feature flag not found");
        return flags();
    }
    public List<Log> logs(int page, boolean errorsOnly) {
        int offset = Math.max(0, Math.min(page, 10000)) * 50;
        return jdbc.query("SELECT * FROM tb_raven_admin_logs " + (errorsOnly ? "WHERE status_code >= 400 " : "")
                + "ORDER BY occurred_at DESC, id DESC LIMIT 50 OFFSET ?", (r, n) -> new Log(r.getString("id"),
                r.getTimestamp("occurred_at").toInstant(), r.getString("actor_id"), r.getString("method"),
                r.getString("path"), r.getInt("status_code"), r.getLong("duration_ms")), offset);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(String actor, String method, String path, int status, long duration) {
        jdbc.update("INSERT INTO tb_raven_admin_logs(id,occurred_at,actor_id,method,path,status_code,duration_ms) VALUES(?,?,?,?,?,?,?)",
                UUID.randomUUID().toString(), Timestamp.from(Instant.now()), actor, method,
                path.substring(0, Math.min(path.length(), 255)), status, duration);
    }
    public boolean blocked(UUID artistId) {
        var rows = jdbc.queryForList("SELECT blocked FROM tb_raven_artists WHERE id = ?", Boolean.class, artistId.toString());
        return rows.isEmpty() || rows.get(0);
    }
}
