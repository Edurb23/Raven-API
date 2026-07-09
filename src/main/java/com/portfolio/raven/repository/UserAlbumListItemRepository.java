package com.portfolio.raven.repository;

import com.portfolio.raven.entity.UserAlbumListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAlbumListItemRepository extends JpaRepository<UserAlbumListItem, UUID> {

    boolean existsByAlbumList_IdAndAlbum_Id(UUID albumListId, UUID albumId);

    Optional<UserAlbumListItem> findByAlbumList_IdAndAlbum_Id(UUID albumListId, UUID albumId);
}
