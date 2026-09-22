package com.portfolio.raven.security;

import com.portfolio.raven.repository.UserRepository;
import com.portfolio.raven.service.token.TokenService;
import jakarta.servlet.DispatcherType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import com.portfolio.raven.entity.Role;
import com.portfolio.raven.entity.User;
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

    @MockBean
    private com.portfolio.raven.service.AdminControlService adminControls;

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
        @org.springframework.web.bind.annotation.PutMapping("/artist/{artistId}/images/{imageId}/select")
        ResponseEntity<Void> selectImage() {
            return ResponseEntity.noContent().build();
        }
        @GetMapping("/artist")
        String artist() {
            return "[]";
        }

        @GetMapping("/error")
        ResponseEntity<Void> error() {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ParameterizedTest
    @CsvSource({"ROLE_ADMIN, 204", "ROLE_USER, 403", "ROLE_ADM, 403"})
    void bearerAuthenticationUsesDatabaseRoleForAdminAccess(String roleName, int expectedStatus) throws Exception {
        var role = new Role();
        role.setName(roleName);
        var account = new User();
        account.setEmail("role-check@example.test");
        account.setRoles(java.util.Set.of(role));
        org.mockito.Mockito.when(tokenService.validateToken("role-test-token")).thenReturn(account.getEmail());
        org.mockito.Mockito.when(userRepository.findByEmail(account.getEmail())).thenReturn(account);

        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put(
                        "/artist/artist-id/images/image-id/select")
                        .header("Authorization", "Bearer role-test-token"))
                .andExpect(status().is(expectedStatus));
    }
}
