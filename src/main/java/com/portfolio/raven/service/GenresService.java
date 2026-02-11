package com.portfolio.raven.service;


import com.portfolio.raven.dto.generoDto.GeneroList;
import com.portfolio.raven.dto.generoDto.GenreDetail;
import com.portfolio.raven.dto.generoDto.RegisterGenero;
import com.portfolio.raven.entity.Genre;
import com.portfolio.raven.mappers.GenresMapper;
import com.portfolio.raven.repository.GenresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
public class GenresService {

    @Autowired
    private GenresRepository genresRepository;

    @Autowired
    private GenresMapper genresMapper;

    public List<GeneroList> listAll(Pageable pageable){
        return genresRepository.findAll(pageable)
                .stream()
                .map(genresMapper::toList)
                .toList();
    }

    public GenreDetail getById(UUID id){
        var genres = genresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genero não encontrado"));
        return genresMapper.toDetailDto(genres);
    }

    public GenreDetail register(RegisterGenero dto){
        boolean exists = genresRepository.existsByNomeIgnoreCase(dto.nome());

        if(exists){
            throw new RuntimeException("Já existe um genero cadastrado com o nome: " + dto.nome());
        }

        Genre genres = genresMapper.toEntity(dto);
        genresRepository.save(genres);
        return genresMapper.toDetailDto(genres);


    }



}
