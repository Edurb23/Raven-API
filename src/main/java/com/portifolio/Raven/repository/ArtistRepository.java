package com.portifolio.Raven.repository;

import com.portifolio.Raven.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {
    boolean existsByNomeArtistIgnoreCase(String nomeArtist);

}
