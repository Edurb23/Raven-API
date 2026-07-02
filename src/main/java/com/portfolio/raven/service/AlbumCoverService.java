package com.portfolio.raven.service;

import com.portfolio.raven.entity.Album;
import com.portfolio.raven.entity.AlbumCover;
import com.portfolio.raven.repository.AlbumCoverRepository;
import com.portfolio.raven.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumCoverService {

    private final AlbumRepository albumRepository;
    private final AlbumCoverRepository albumCoverRepository;

    public String saveCoverAsBase64(MultipartFile file, UUID albumId) {
        try {
            Album album = albumRepository.findById(albumId)
                    .orElseThrow(() -> new RuntimeException("Album not found with ID: " + albumId));

            String base64Cover = Base64.getEncoder().encodeToString(file.getBytes());
            boolean shouldSelect = albumCoverRepository.findByAlbumIdAndSelectedTrue(albumId).isEmpty();

            AlbumCover cover = new AlbumCover();
            cover.setAlbum(album);
            cover.setUrlImage(base64Cover);
            cover.setSelected(shouldSelect);
            albumCoverRepository.save(cover);

            if (shouldSelect) {
                album.setCoverUrl(base64Cover);
                albumRepository.save(album);
            }

            return "Cover successfully saved for album: " + album.getName();
        } catch (IOException e) {
            throw new RuntimeException("Error converting cover to Base64: " + e.getMessage());
        }
    }

    public String selectAlbumCover(UUID albumId, UUID coverId) {
        AlbumCover selectedCover = albumCoverRepository.findById(coverId)
                .orElseThrow(() -> new RuntimeException("Album cover not found with ID: " + coverId));

        if (!selectedCover.getAlbum().getId().equals(albumId)) {
            throw new RuntimeException("Cover does not belong to album: " + albumId);
        }

        albumCoverRepository.findByAlbumId(albumId)
                .forEach(cover -> {
                    cover.setSelected(cover.getId().equals(coverId));
                    albumCoverRepository.save(cover);
                });

        Album album = selectedCover.getAlbum();
        album.setCoverUrl(selectedCover.getUrlImage());
        albumRepository.save(album);

        return "Selected cover updated for album: " + album.getName();
    }
}
