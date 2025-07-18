package com.portifolio.Raven.service;

import com.portifolio.Raven.entity.Artist;
import com.portifolio.Raven.entity.ArtistImage;
import com.portifolio.Raven.repository.ArtistImageRepository;
import com.portifolio.Raven.repository.ArtistRepository;
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
                    .orElseThrow(() -> new RuntimeException("Artist não encontrado com ID: " + artistId));


            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);


            ArtistImage artistImage = new ArtistImage();
            artistImage.setArtist(artist);
            artistImage.setUrlImage(base64Image);

            artistImageRepository.save(artistImage);

            return "Imagem salva com sucesso para artista: " + artist.getNomeArtist();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter imagem para base64: " + e.getMessage());
        }
    }


}
