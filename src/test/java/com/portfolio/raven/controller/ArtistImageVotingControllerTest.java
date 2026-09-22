package com.portfolio.raven.controller;

import com.portfolio.raven.dto.artistDto.ArtistImageVoting;
import com.portfolio.raven.entity.User;
import com.portfolio.raven.repository.UserRepository;
import com.portfolio.raven.security.SecurityConfigurations;
import com.portfolio.raven.security.SecurityFilter;
import com.portfolio.raven.service.ArtistImageVotingService;
import com.portfolio.raven.service.token.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistImageVotingController.class)
@Import({SecurityConfigurations.class, SecurityFilter.class})
class ArtistImageVotingControllerTest {
    @Autowired MockMvc mvc;
    @MockBean ArtistImageVotingService voting;
    @MockBean TokenService tokenService;
    @MockBean UserRepository userRepository;
    @MockBean com.portfolio.raven.service.AdminControlService adminControls;

    @Test
    void readsAndSavesVotesForTheAuthenticatedUserOnly() throws Exception {
        var artist = UUID.randomUUID();
        var image = UUID.randomUUID();
        var listener = new User();
        listener.setId(UUID.randomUUID());
        listener.setEmail("listener@example.test");
        listener.setRoles(Set.of());
        var result = new ArtistImageVoting(LocalDate.of(2026, 9, 21),
                Instant.parse("2026-09-28T03:00:00Z"), "America/Sao_Paulo", image,
                List.of(new ArtistImageVoting.ImageVotes(image, 1, false)));
        when(voting.getVoting(artist, listener.getId())).thenReturn(result);
        when(voting.vote(artist, image, listener.getId())).thenReturn(result);

        mvc.perform(get("/artist/{id}/images/votes", artist).with(user(listener)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.votedImageId").value(image.toString()));
        // Even a supplied userId cannot override the authenticated principal.
        mvc.perform(put("/artist/{id}/images/{image}/vote", artist, image).with(user(listener))
                        .contentType("application/json").content("{\"userId\":\"someone-else\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.images[0].votes").value(1));
        verify(voting).vote(artist, image, listener.getId());
    }

    @Test
    void anonymousUsersCannotReadOrCastVotes() throws Exception {
        mvc.perform(get("/artist/{id}/images/votes", UUID.randomUUID())).andExpect(status().isUnauthorized());
        mvc.perform(put("/artist/{id}/images/{image}/vote", UUID.randomUUID(), UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(voting);
    }
}
