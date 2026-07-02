package com.portifolio.Raven.repository;

import com.portifolio.Raven.entity.UserAlbumList;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAlbumListRepository extends JpaRepository<UserAlbumList, UUID> {

    @EntityGraph(attributePaths = {"items", "items.album", "items.album.artist"})
    List<UserAlbumList> findByUserId(UUID userId);

    @EntityGraph(attributePaths = {"items", "items.album", "items.album.artist"})
    Optional<UserAlbumList> findByIdAndUserId(UUID id, UUID userId);
}
