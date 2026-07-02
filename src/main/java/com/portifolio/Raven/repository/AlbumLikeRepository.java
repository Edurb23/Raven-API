package com.portifolio.Raven.repository;

import com.portifolio.Raven.entity.AlbumLike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlbumLikeRepository extends JpaRepository<AlbumLike, UUID> {

    boolean existsByUserIdAndAlbumId(UUID userId, UUID albumId);

    @EntityGraph(attributePaths = {"album", "album.artist"})
    List<AlbumLike> findByUserId(UUID userId);

    Optional<AlbumLike> findByUserIdAndAlbumId(UUID userId, UUID albumId);
}
