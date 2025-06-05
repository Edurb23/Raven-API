package com.portifolio.Raven.model;


import com.portifolio.Raven.dto.artistDto.RegisterArtistDto;
import com.portifolio.Raven.dto.artistDto.UpdateArtistDto;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "artists")
public class Artist {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String nomeArtist;

    @Column(name= "genre", nullable = false)
    private String genero;
    @Lob
    @Column(name= "bio", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String biografia;



    public Artist(@Valid RegisterArtistDto registerArtistDto){
        nomeArtist = registerArtistDto.nomeArtist();
        genero = registerArtistDto.genero();
        biografia = registerArtistDto.biografia();
    }


    public void updateArtist(UpdateArtistDto dto) {
        if(dto.nomeArtist() != null)
            nomeArtist = dto.nomeArtist();
        if(dto.genero() != null)
            genero = dto.genero();
        if(dto.biografia() != null)
            biografia = dto.biografia();

    }
}
