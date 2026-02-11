package com.portfolio.raven.repository;

import com.portfolio.raven.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GeneroRepository extends JpaRepository<Genero, UUID> {

    Optional<Genero> findByNome(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}
