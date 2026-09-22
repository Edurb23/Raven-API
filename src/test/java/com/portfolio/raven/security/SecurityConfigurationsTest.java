package com.portfolio.raven.security;

import com.portfolio.raven.repository.UserRepository;
import com.portfolio.raven.service.token.TokenService;
import jakarta.servlet.DispatcherType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = SecurityConfigurationsTest.Endpoints.class)
@Import({SecurityConfigurations.class, SecurityFilter.class})
class SecurityConfigurationsTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void artistRequiresAuthentication() throws Exception {
        mvc.perform(get("/artist")).andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedUserCanAccessArtist() throws Exception {
        mvc.perform(get("/artist").with(user("listener"))).andExpect(status().isOk());
    }

    @Test
    void errorDispatchPreservesServerErrorWithoutAuthentication() throws Exception {
        mvc.perform(get("/error").with(request -> {
            request.setDispatcherType(DispatcherType.ERROR);
            return request;
        })).andExpect(status().isInternalServerError());
    }

    @Test
    void directErrorRequestStillRequiresAuthentication() throws Exception {
        mvc.perform(get("/error")).andExpect(status().isUnauthorized());
    }

    @Test
    void weeklyVotingRequiresAuthenticationAndManualSelectionRequiresAdmin() throws Exception {
        mvc.perform(get("/artist/artist-id/images/votes")).andExpect(status().isUnauthorized());
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/artist/artist-id/images/image-id/vote"))
                .andExpect(status().isUnauthorized());
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/artist/artist-id/images/image-id/select")
                .with(user("listener"))).andExpect(status().isForbidden());
    }

    @RestController
    static class Endpoints {
        @GetMapping("/artist")
        String artist() {
            return "[]";
        }

        @GetMapping("/error")
        ResponseEntity<Void> error() {
            return ResponseEntity.internalServerError().build();
        }
    }
}
