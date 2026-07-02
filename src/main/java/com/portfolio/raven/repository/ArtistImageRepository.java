package com.portfolio.raven.repository;

import com.portfolio.raven.entity.ArtistImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArtistImageRepository extends JpaRepository<ArtistImage, UUID> {

    List<ArtistImage> findByArtistId(UUID artistId);

    Optional<ArtistImage> findByArtistIdAndSelectedTrue(UUID artistId);
}
