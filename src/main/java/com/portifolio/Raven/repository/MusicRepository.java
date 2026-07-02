package com.portifolio.Raven.repository;

import com.portifolio.Raven.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MusicRepository extends JpaRepository<Music, UUID> {

    @Override
    @EntityGraph(attributePaths = {"artist", "album"})
    Page<Music> findAll(Pageable pageable);

    @Query("SELECT m FROM Music m JOIN FETCH m.artist LEFT JOIN FETCH m.album WHERE m.id = :id")
    Optional<Music> findWithArtistAndAlbum(@Param("id") UUID id);
}
