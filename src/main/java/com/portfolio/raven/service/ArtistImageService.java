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


    public String saveImageAsBase64(MultipartFile file, UUID artistId){
        try {
            Artist artist = artistRepository.findById(artistId)
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

    public String selectArtistImage(UUID artistId, UUID imageId) {
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
