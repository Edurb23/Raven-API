--liquibase formatted sql
--changeset raven:010-artist-banner
ALTER TABLE tb_raven_artists ADD COLUMN banner_image MEDIUMTEXT NULL;
