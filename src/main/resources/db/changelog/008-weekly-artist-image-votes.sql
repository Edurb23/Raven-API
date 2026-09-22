--liquibase formatted sql
--changeset raven:008-weekly-artist-image-votes
CREATE TABLE tb_raven_artist_image_votes (
    artist_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    week_start DATE NOT NULL,
    image_id VARCHAR(36) NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (artist_id, user_id, week_start),
    CONSTRAINT fk_image_vote_artist FOREIGN KEY (artist_id) REFERENCES tb_raven_artists(id) ON DELETE CASCADE,
    CONSTRAINT fk_image_vote_user FOREIGN KEY (user_id) REFERENCES tb_raven_users(id) ON DELETE CASCADE,
    CONSTRAINT fk_image_vote_image FOREIGN KEY (image_id) REFERENCES tb_raven_artist_image(id) ON DELETE CASCADE
);
CREATE INDEX idx_image_votes_week ON tb_raven_artist_image_votes(week_start, artist_id);

CREATE TABLE tb_raven_artist_image_elections (
    artist_id VARCHAR(36) NOT NULL,
    week_start DATE NOT NULL,
    winner_image_id VARCHAR(36),
    processed_at TIMESTAMP NOT NULL,
    PRIMARY KEY (artist_id, week_start),
    CONSTRAINT fk_image_election_artist FOREIGN KEY (artist_id) REFERENCES tb_raven_artists(id) ON DELETE CASCADE,
    CONSTRAINT fk_image_election_winner FOREIGN KEY (winner_image_id) REFERENCES tb_raven_artist_image(id) ON DELETE SET NULL
);
