package com.portfolio.raven.repository;

import com.portfolio.raven.entity.AlbumReview;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlbumReviewRepository extends JpaRepository<AlbumReview, UUID> {

    @EntityGraph(attributePaths = {"user", "album", "album.artist"})
    List<AlbumReview> findByUserId(UUID userId);

    @EntityGraph(attributePaths = {"user", "album", "album.artist"})
    Optional<AlbumReview> findByUserIdAndAlbumId(UUID userId, UUID albumId);
}
