package com.portfolio.raven.mappers;

import com.portfolio.raven.dto.generoDto.ArtistGeneroDto;
import com.portfolio.raven.dto.generoDto.GeneroDetail;
import com.portfolio.raven.dto.generoDto.GeneroList;
import com.portfolio.raven.dto.generoDto.RegisterGenero;
import com.portfolio.raven.entity.Genero;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GeneroMapper {

   public Genero toEntity(RegisterGenero dto){
       Genero genero = new Genero();
       genero.setNome(dto.nome());
       return genero;
   }

   public GeneroDetail toDetailDto(Genero genero){
       return new GeneroDetail(genero.getId(),genero.getNome(),genero.getArtists());
   }

    public GeneroList toList(Genero genero) {
        Set<ArtistGeneroDto> artistDtos = genero.getArtists().stream()
                .map(artist -> new ArtistGeneroDto(artist.getId(), artist.getNomeArtist()))
                .collect(Collectors.toSet());

        return new GeneroList(genero.getId(), genero.getNome(), artistDtos);
    }



}
