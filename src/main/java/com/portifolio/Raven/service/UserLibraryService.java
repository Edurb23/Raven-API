package com.portifolio.Raven.service;

import com.portifolio.Raven.dto.albumDto.AlbumDetail;
import com.portifolio.Raven.dto.artistDto.ArtistListDto;
import com.portifolio.Raven.dto.userDto.AlbumReviewDetail;
import com.portifolio.Raven.dto.userDto.AlbumReviewRequest;
import com.portifolio.Raven.dto.userDto.CreateAlbumListDto;
import com.portifolio.Raven.dto.userDto.UserAlbumListDetail;
import com.portifolio.Raven.entity.*;
import com.portifolio.Raven.mappers.AlbumMapper;
import com.portifolio.Raven.mappers.ArtistMapper;
import com.portifolio.Raven.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserLibraryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumReviewRepository albumReviewRepository;

    @Autowired
    private AlbumLikeRepository albumLikeRepository;

    @Autowired
    private UserAlbumListRepository userAlbumListRepository;

    @Autowired
    private UserAlbumListItemRepository userAlbumListItemRepository;

    @Autowired
    private FavoriteArtistRepository favoriteArtistRepository;

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private ArtistMapper artistMapper;

    @Transactional
    public AlbumReviewDetail reviewAlbum(UUID userId, UUID albumId, AlbumReviewRequest dto) {
        User user = findUser(userId);
        Album album = findAlbum(albumId);

        AlbumReview review = albumReviewRepository.findByUserIdAndAlbumId(userId, albumId)
                .orElseGet(AlbumReview::new);
        review.setUser(user);
        review.setAlbum(album);
        review.setRating(dto.rating());
        review.setComment(dto.comment());

        albumReviewRepository.save(review);
        return toReviewDetail(review);
    }

    public List<AlbumReviewDetail> listReviews(UUID userId) {
        return albumReviewRepository.findByUserId(userId).stream()
                .map(this::toReviewDetail)
                .toList();
    }

    @Transactional
    public void likeAlbum(UUID userId, UUID albumId) {
        if (albumLikeRepository.existsByUserIdAndAlbumId(userId, albumId)) {
            return;
        }

        AlbumLike like = new AlbumLike();
        like.setUser(findUser(userId));
        like.setAlbum(findAlbum(albumId));
        albumLikeRepository.save(like);
    }

    @Transactional
    public void unlikeAlbum(UUID userId, UUID albumId) {
        albumLikeRepository.findByUserIdAndAlbumId(userId, albumId)
                .ifPresent(albumLikeRepository::delete);
    }

    public List<AlbumDetail> listLikedAlbums(UUID userId) {
        return albumLikeRepository.findByUserId(userId).stream()
                .map(AlbumLike::getAlbum)
                .map(albumMapper::toDetail)
                .toList();
    }

    @Transactional
    public UserAlbumListDetail createAlbumList(UUID userId, CreateAlbumListDto dto) {
        UserAlbumList list = new UserAlbumList();
        list.setUser(findUser(userId));
        list.setName(dto.name());
        list.setDescription(dto.description());
        userAlbumListRepository.save(list);
        return toListDetail(list);
    }

    public List<UserAlbumListDetail> listAlbumLists(UUID userId) {
        return userAlbumListRepository.findByUserId(userId).stream()
                .map(this::toListDetail)
                .toList();
    }

    @Transactional
    public void deleteAlbumList(UUID userId, UUID listId) {
        UserAlbumList list = findUserList(userId, listId);
        userAlbumListRepository.delete(list);
    }

    @Transactional
    public UserAlbumListDetail addAlbumToList(UUID userId, UUID listId, UUID albumId) {
        UserAlbumList list = findUserList(userId, listId);
        if (!userAlbumListItemRepository.existsByAlbumList_IdAndAlbum_Id(listId, albumId)) {
            UserAlbumListItem item = new UserAlbumListItem();
            item.setAlbumList(list);
            item.setAlbum(findAlbum(albumId));
            userAlbumListItemRepository.save(item);
        }
        return toListDetail(findUserList(userId, listId));
    }

    @Transactional
    public void removeAlbumFromList(UUID userId, UUID listId, UUID albumId) {
        findUserList(userId, listId);
        userAlbumListItemRepository.findByAlbumList_IdAndAlbum_Id(listId, albumId)
                .ifPresent(userAlbumListItemRepository::delete);
    }

    @Transactional
    public void favoriteArtist(UUID userId, UUID artistId) {
        if (favoriteArtistRepository.existsByUserIdAndArtistId(userId, artistId)) {
            return;
        }

        FavoriteArtist favorite = new FavoriteArtist();
        favorite.setUser(findUser(userId));
        favorite.setArtist(findArtist(artistId));
        favoriteArtistRepository.save(favorite);
    }

    @Transactional
    public void unfavoriteArtist(UUID userId, UUID artistId) {
        favoriteArtistRepository.findByUserIdAndArtistId(userId, artistId)
                .ifPresent(favoriteArtistRepository::delete);
    }

    public List<ArtistListDto> listFavoriteArtists(UUID userId) {
        return favoriteArtistRepository.findByUserId(userId).stream()
                .map(FavoriteArtist::getArtist)
                .map(artistMapper::toList)
                .toList();
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
    }

    private Album findAlbum(UUID albumId) {
        return albumRepository.findWithArtist(albumId)
                .orElseThrow(() -> new RuntimeException("Album nao encontrado"));
    }

    private Artist findArtist(UUID artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artista nao encontrado"));
    }

    private UserAlbumList findUserList(UUID userId, UUID listId) {
        return userAlbumListRepository.findByIdAndUserId(listId, userId)
                .orElseThrow(() -> new RuntimeException("Lista nao encontrada para esse usuario"));
    }

    private AlbumReviewDetail toReviewDetail(AlbumReview review) {
        return new AlbumReviewDetail(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getUsername(),
                review.getAlbum().getId(),
                review.getAlbum().getName(),
                review.getRating(),
                review.getComment(),
                review.getCreated_at(),
                review.getUpdate_at()
        );
    }

    private UserAlbumListDetail toListDetail(UserAlbumList list) {
        List<AlbumDetail> albums = list.getItems().stream()
                .map(UserAlbumListItem::getAlbum)
                .map(albumMapper::toDetail)
                .toList();

        return new UserAlbumListDetail(
                list.getId(),
                list.getUser().getId(),
                list.getName(),
                list.getDescription(),
                albums,
                list.getCreated_at(),
                list.getUpdate_at()
        );
    }
}
