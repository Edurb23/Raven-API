package com.portfolio.raven.repository;

import com.portfolio.raven.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AlbumRepository extends JpaRepository<Album, UUID> {

    @Override
    @EntityGraph(attributePaths = "artist")
    Page<Album> findAll(Pageable pageable);

    @Query("SELECT a FROM Album a JOIN FETCH a.artist WHERE a.id = :id")
    Optional<Album> findWithArtist(@Param("id") UUID id);
}
