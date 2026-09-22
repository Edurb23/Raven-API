--liquibase formatted sql
--changeset raven:009-admin-console
ALTER TABLE tb_raven_artists ADD COLUMN blocked BOOLEAN NOT NULL DEFAULT FALSE;
CREATE TABLE tb_raven_feature_flags (
    flag_key VARCHAR(80) PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
INSERT INTO tb_raven_feature_flags VALUES
 ('artist_catalog', 'Artist catalog and public artist pages', TRUE, CURRENT_TIMESTAMP),
 ('artist_photo_voting', 'Weekly photo voting', TRUE, CURRENT_TIMESTAMP),
 ('artist_photo_uploads', 'Upload new artist photos', TRUE, CURRENT_TIMESTAMP);
CREATE TABLE tb_raven_admin_logs (
    id VARCHAR(36) PRIMARY KEY,
    occurred_at TIMESTAMP NOT NULL,
    actor_id VARCHAR(36),
    method VARCHAR(10) NOT NULL,
    path VARCHAR(255) NOT NULL,
    status_code INT NOT NULL,
    duration_ms BIGINT NOT NULL
);
CREATE INDEX idx_admin_logs_time ON tb_raven_admin_logs(occurred_at);
