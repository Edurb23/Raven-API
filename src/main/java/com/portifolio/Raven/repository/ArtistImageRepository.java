package com.portifolio.Raven.repository;

import com.portifolio.Raven.model.ArtistImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtistImageRepository extends JpaRepository<ArtistImage, UUID> {
}
