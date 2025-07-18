package com.portifolio.Raven.mappers;

import com.portifolio.Raven.dto.generoDto.GeneroDetail;
import com.portifolio.Raven.dto.generoDto.GeneroList;
import com.portifolio.Raven.dto.generoDto.RegisterGenero;
import com.portifolio.Raven.entity.Genero;
import org.springframework.stereotype.Component;

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

   public GeneroList toList(Genero genero){
       return new GeneroList(genero.getId(),genero.getNome(),genero.getArtists());
   }


}
