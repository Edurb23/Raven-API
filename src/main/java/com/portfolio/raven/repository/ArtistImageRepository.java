package com.portfolio.raven.repository;

import com.portfolio.raven.entity.ArtistImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtistImageRepository extends JpaRepository<ArtistImage, UUID> {
}
