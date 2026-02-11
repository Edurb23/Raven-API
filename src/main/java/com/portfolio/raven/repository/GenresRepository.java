package com.portfolio.raven.repository;

import com.portfolio.raven.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GenresRepository extends JpaRepository<Genre, UUID> {

    Optional<Genre> findByNome(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}
