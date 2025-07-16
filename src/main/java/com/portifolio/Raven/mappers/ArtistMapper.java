package com.portifolio.Raven.mappers;

import com.portifolio.Raven.dto.artistDto.ArtistDetail;
import com.portifolio.Raven.dto.artistDto.ArtistListDto;
import com.portifolio.Raven.dto.artistDto.RegisterArtistDto;
import com.portifolio.Raven.entity.Artist;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper {

    public Artist toEntity(RegisterArtistDto dto){
        Artist artist = new Artist();
        artist.setNomeArtist(dto.nomeArtist());
        artist.setBiografia(dto.biografia());
        artist.setGenero(dto.genero());
        return artist;
    }


    public ArtistDetail toDetailDto(Artist artist){
        return new ArtistDetail(artist.getId(),artist.getNomeArtist(),artist.getBiografia(), artist.getGenero(), artist.getArtistImages());
    }

    public ArtistListDto toList(Artist artist){
        return  new ArtistListDto(artist.getId(),artist.getNomeArtist(),artist.getBiografia(), artist.getGenero());
    }


}
