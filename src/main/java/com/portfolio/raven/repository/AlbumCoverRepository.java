package com.portfolio.raven.repository;

import com.portfolio.raven.entity.AlbumCover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlbumCoverRepository extends JpaRepository<AlbumCover, UUID> {

    List<AlbumCover> findByAlbumId(UUID albumId);

    Optional<AlbumCover> findByAlbumIdAndSelectedTrue(UUID albumId);
}
