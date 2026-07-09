package com.portfolio.raven.repository;

import com.portfolio.raven.entity.FavoriteArtist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, UUID> {

    boolean existsByUserIdAndArtistId(UUID userId, UUID artistId);

    @EntityGraph(attributePaths = {"artist", "artist.generos", "artist.artistImages"})
    List<FavoriteArtist> findByUserId(UUID userId);

    Optional<FavoriteArtist> findByUserIdAndArtistId(UUID userId, UUID artistId);
}
