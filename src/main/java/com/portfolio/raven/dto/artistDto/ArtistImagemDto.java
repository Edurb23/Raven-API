package com.portfolio.raven.dto.artistDto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record ArtistImagemDto(UUID artistId, MultipartFile file) {}

