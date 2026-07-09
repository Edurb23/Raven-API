package com.portfolio.raven.service;

import com.portfolio.raven.dto.albumDto.AlbumDetail;
import com.portfolio.raven.dto.albumDto.RegisterAlbumDto;
import com.portfolio.raven.dto.albumDto.UpdateAlbumDto;
import com.portfolio.raven.mappers.AlbumMapper;
import com.portfolio.raven.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumMapper albumMapper;

    public List<AlbumDetail> listAll(Pageable pageable) {
        return albumRepository.findAll(pageable).stream()
                .map(albumMapper::toDetail)
                .toList();
    }

    public AlbumDetail findById(UUID id) {
        var album = albumRepository.findWithArtist(id)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        return albumMapper.toDetail(album);
    }

    @Transactional
    public AlbumDetail register(RegisterAlbumDto dto) {
        var album = albumMapper.toEntity(dto);
        albumRepository.save(album);
        return albumMapper.toDetail(album);
    }

    @Transactional
    public AlbumDetail update(UUID id, UpdateAlbumDto dto) {
        var album = albumRepository.findWithArtist(id)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        albumMapper.update(dto, album);
        return albumMapper.toDetail(album);
    }

    public void delete(UUID id) {
        albumRepository.deleteById(id);
    }
}
