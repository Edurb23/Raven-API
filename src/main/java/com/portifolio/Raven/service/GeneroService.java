package com.portifolio.Raven.service;

import com.portifolio.Raven.dto.generoDto.GeneroDetail;
import com.portifolio.Raven.dto.generoDto.GeneroList;
import com.portifolio.Raven.dto.generoDto.RegisterGenero;
import com.portifolio.Raven.entity.Genero;
import com.portifolio.Raven.mappers.GeneroMapper;
import com.portifolio.Raven.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private GeneroMapper generoMapper;

    public List<GeneroList> listAll(Pageable pageable){
        return generoRepository.findAll(pageable)
                .stream()
                .map(generoMapper::toList)
                .toList();
    }

    public GeneroDetail getById(UUID id){
        var genero = generoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genero não encontrado"));
        return generoMapper.toDetailDto(genero);
    }

    public GeneroDetail register(RegisterGenero dto){
        boolean exists = generoRepository.existsByNomeIgnoreCase(dto.nome());

        if(exists){
            throw new RuntimeException("Já existe um genero cadastrado com o nome: " + dto.nome());
        }

        Genero genero = generoMapper.toEntity(dto);
        generoRepository.save(genero);
        return generoMapper.toDetailDto(genero);


    }



}
