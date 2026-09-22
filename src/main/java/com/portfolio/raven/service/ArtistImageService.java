package com.portfolio.raven.service;

import com.portfolio.raven.entity.Artist;
import com.portfolio.raven.entity.ArtistImage;
import com.portfolio.raven.repository.ArtistImageRepository;
import com.portfolio.raven.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistImageService {

   private final ArtistRepository artistRepository;
   private final ArtistImageRepository artistImageRepository;


    @org.springframework.transaction.annotation.Transactional
    public String saveImageAsBase64(MultipartFile file, UUID artistId){
        try {
            validateImage(file);
            Artist artist = artistRepository.findLockedById(artistId)
                    .orElseThrow(() -> new RuntimeException("Artist not found with ID: " + artistId));


            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);


            ArtistImage artistImage = new ArtistImage();
            artistImage.setArtist(artist);
            artistImage.setUrlImage(base64Image);
            artistImage.setSelected(artistImageRepository.findByArtistIdAndSelectedTrue(artistId).isEmpty());

            artistImageRepository.save(artistImage);

            return "Image successfully saved for artist:  " + artist.getName();

        } catch (IOException e) {
            throw new RuntimeException("Error converting image to Base64:  " + e.getMessage());
        }
    }

    private void validateImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty() || file.getSize() > 5 * 1024 * 1024) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Choose an image up to 5 MB");
        }
        try (var input = javax.imageio.ImageIO.createImageInputStream(file.getInputStream())) {
            var readers = javax.imageio.ImageIO.getImageReaders(input);
            if (!readers.hasNext()) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Choose a JPEG, PNG or GIF image");
            var reader = readers.next();
            try {
                if (!java.util.Set.of("jpeg", "jpg", "png", "gif").contains(reader.getFormatName().toLowerCase(java.util.Locale.ROOT)))
                    throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Choose a JPEG, PNG or GIF image");
                reader.setInput(input);
                if ((long) reader.getWidth(0) * reader.getHeight(0) > 40_000_000L)
                    throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Image dimensions are too large");
            } finally { reader.dispose(); }
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void saveBanner(MultipartFile file, UUID artistId) {
        try {
            validateImage(file);
            var artist = artistRepository.findLockedById(artistId)
                    .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Artist not found"));
            artist.setBannerImage(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Could not read the image", e);
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void removeBanner(UUID artistId) {
        var artist = artistRepository.findLockedById(artistId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Artist not found"));
        artist.setBannerImage(null);
    }

    @org.springframework.transaction.annotation.Transactional
    public void removeImage(UUID artistId, UUID imageId) {
        var artist = artistRepository.findLockedById(artistId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Artist not found"));
        var image = artistImageRepository.findById(imageId)
                .filter(value -> value.getArtist().getId().equals(artistId))
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Artist image not found"));

        // Detach from the managed collection so cascading persistence cannot restore it.
        artist.getArtistImages().removeIf(value -> value.getId().equals(imageId));
        if (Boolean.TRUE.equals(image.getSelected())) {
            artist.getArtistImages().stream()
                    .min(java.util.Comparator.comparing(ArtistImage::getCreated_at)
                            .thenComparing(value -> value.getId().toString()))
                    .ifPresent(replacement -> replacement.setSelected(true));
        }
        // Foreign keys delete this image's votes and clear historical winner references.
        artistImageRepository.delete(image);
    }

    @org.springframework.transaction.annotation.Transactional
    public String selectArtistImage(UUID artistId, UUID imageId) {
        artistRepository.findLockedById(artistId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Artist not found"));
        ArtistImage selectedImage = artistImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Artist image not found with ID: " + imageId));

        if (!selectedImage.getArtist().getId().equals(artistId)) {
            throw new RuntimeException("Image does not belong to artist: " + artistId);
        }

        artistImageRepository.findByArtistId(artistId)
                .forEach(image -> {
                    image.setSelected(image.getId().equals(imageId));
                    artistImageRepository.save(image);
                });

        return "Selected image updated for artist: " + selectedImage.getArtist().getName();
    }


}
