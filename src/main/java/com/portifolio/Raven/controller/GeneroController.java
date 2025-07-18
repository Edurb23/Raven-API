package com.portifolio.Raven.controller;

import com.portifolio.Raven.dto.generoDto.GeneroDetail;
import com.portifolio.Raven.dto.generoDto.GeneroList;
import com.portifolio.Raven.dto.generoDto.RegisterGenero;
import com.portifolio.Raven.service.GeneroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("genero")
public class GeneroController {


    @Autowired
    private GeneroService generoService;


    @GetMapping("/list")
    public ResponseEntity<List<GeneroList>> listALl(Pageable pageable){
        var ListGenero = generoService.listAll(pageable).stream().toList();
        return ok(ListGenero);
    }

    @GetMapping("{id}")
    public ResponseEntity<GeneroDetail> getByID(@PathVariable("id")UUID id) {
        var generoDetail = generoService.getById(id);
        return ok(generoDetail);
    }


    @PostMapping("/register")
    @Transactional
    public ResponseEntity<GeneroDetail> post(@RequestBody @Valid RegisterGenero dto,
                                             UriComponentsBuilder uriComponentsBuilder){
        var generoDetail = generoService.register(dto);
        var uri = uriComponentsBuilder.path("/genero/{id}").buildAndExpand(generoDetail.id()).toUri();
        return ResponseEntity.created(uri).body(generoDetail);


    }



}
