package com.portfolio.raven.repository;

import com.portfolio.raven.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {


    boolean existsByNameIgnoreCase(String name);
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Artist a WHERE a.id = :id")
    Optional<Artist> findLockedById(@Param("id") UUID id);
    org.springframework.data.domain.Page<Artist> findByBlockedFalse(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT a FROM Artist a LEFT JOIN FETCH a.genres WHERE a.id = :id")
    Optional<Artist> findWithGenres(@Param("id") UUID id);

}
