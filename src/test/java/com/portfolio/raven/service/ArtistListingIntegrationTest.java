package com.portfolio.raven.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.raven.mappers.ArtistMapper;
import com.portfolio.raven.repository.ArtistRepository;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@SpringBootTest(properties = "spring.liquibase.enabled=false")
class ArtistListingIntegrationTest {
    @Autowired
    private ArtistRepository repository;
    @Autowired
    private ArtistMapper mapper;
    @Autowired
    private ArtistService service;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listingInitializesRelationsBeforeReturningResponse() {
        var page = PageRequest.of(0, 20);
        var detachedArtists = repository.findAll(page);
        assumeFalse(detachedArtists.isEmpty(), "Requires an existing artist; this test does not write data");

        // Reproduce the previous flow without a surrounding service transaction.
        assertThrows(LazyInitializationException.class,
                () -> mapper.toList(detachedArtists.getContent().get(0)));

        assertDoesNotThrow(() -> objectMapper.writeValueAsString(service.listAll(page)));
    }
}
