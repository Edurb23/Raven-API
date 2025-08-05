package com.portifolio.Raven.repository;

import com.portifolio.Raven.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {
    boolean existsByNomeArtistIgnoreCase(String nomeArtist);

    @Query("SELECT a FROM Artist a JOIN FETCH a.generos WHERE a.id = :id")
    Optional<Artist> findWithGeneros(@Param("id") UUID id);

}
