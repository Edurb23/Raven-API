package com.portfolio.raven.repository;

import com.portfolio.raven.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {


    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT a FROM Artist a JOIN FETCH a.genres WHERE a.id = :id")
    Optional<Artist> findWithGenres(@Param("id") UUID id);

}
