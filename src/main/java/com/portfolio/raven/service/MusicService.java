package com.portfolio.raven.service;

import com.portfolio.raven.dto.musicDto.MusicDetail;
import com.portfolio.raven.dto.musicDto.RegisterMusicDto;
import com.portfolio.raven.dto.musicDto.UpdateMusicDto;
import com.portfolio.raven.mappers.MusicMapper;
import com.portfolio.raven.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MusicService {

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private MusicMapper musicMapper;

    public List<MusicDetail> listAll(Pageable pageable) {
        return musicRepository.findAll(pageable).stream()
                .map(musicMapper::toDetail)
                .toList();
    }

    public MusicDetail findById(UUID id) {
        var music = musicRepository.findWithArtistAndAlbum(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        return musicMapper.toDetail(music);
    }

    @Transactional
    public MusicDetail register(RegisterMusicDto dto) {
        var music = musicMapper.toEntity(dto);
        musicRepository.save(music);
        return musicMapper.toDetail(music);
    }

    @Transactional
    public MusicDetail update(UUID id, UpdateMusicDto dto) {
        var music = musicRepository.findWithArtistAndAlbum(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        musicMapper.update(dto, music);
        return musicMapper.toDetail(music);
    }

    public void delete(UUID id) {
        musicRepository.deleteById(id);
    }
}
