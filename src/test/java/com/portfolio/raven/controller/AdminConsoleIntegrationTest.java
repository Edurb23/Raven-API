package com.portfolio.raven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.raven.service.AdminControlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "raven.image-voting.scheduler.enabled=false")
@AutoConfigureMockMvc
@Transactional
class AdminConsoleIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;
    @Autowired ObjectMapper json;
    @Autowired jakarta.persistence.EntityManager entityManager;
    @SpyBean AdminControlService controls;
    private UUID genre;

    @BeforeEach void prepare() {
        // Keep all test fixtures transactional, including request audit side effects.
        doNothing().when(controls).record(nullable(String.class), anyString(), anyString(), anyInt(), anyLong());
        genre=UUID.randomUUID();
        jdbc.update("INSERT INTO tb_raven_genero(id,nome) VALUES(?,?)",genre.toString(),"Admin test genre");
        jdbc.update("UPDATE tb_raven_feature_flags SET enabled=TRUE");
    }

    private String createArtist() throws Exception {
        String result=mvc.perform(post("/admin/artists").with(user("admin").roles("ADMIN"))
                .contentType("application/json").content(json.writeValueAsString(Map.of(
                        "name","Admin test "+UUID.randomUUID(),"bio","Test biography","genres",List.of(genre)))))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return json.readTree(result).path("artist").path("id").asText();
    }

    @Test void adminCanCreateEditBlockUnblockAndManagePhotos() throws Exception {
        String id=createArtist();
        mvc.perform(put("/admin/artists/"+id).with(user("admin").roles("ADMIN"))
                .contentType("application/json").content(json.writeValueAsString(Map.of(
                        "name","Updated admin test "+id,"bio","Changed biography","genres",List.of(genre)))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.artist.name").value("Updated admin test "+id))
                .andExpect(jsonPath("$.artist.bio").value("Changed biography"));
        mvc.perform(patch("/admin/artists/"+id+"/blocked").with(user("admin").roles("ADMIN"))
                .contentType("application/json").content("{\"blocked\":true}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.blocked").value(true));
        mvc.perform(get("/artist/"+id).with(user("listener"))).andExpect(status().isNotFound());
        mvc.perform(get("/artist/"+id+"/images/votes").with(user("listener"))).andExpect(status().isNotFound());
        mvc.perform(get("/admin/artists/"+id).with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
        mvc.perform(patch("/admin/artists/"+id+"/blocked").with(user("admin").roles("ADMIN"))
                .contentType("application/json").content("{\"blocked\":false}"))
                .andExpect(status().isOk());
        mvc.perform(get("/artist/"+id).with(user("listener"))).andExpect(status().isOk());

        byte[] png=Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+aM1sAAAAASUVORK5CYII=");
        for(int i=0;i<2;i++) mvc.perform(multipart("/admin/artists/"+id+"/images")
                .file(new MockMultipartFile("file","photo.png","image/png",png)).with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        entityManager.flush();
        String imageId=jdbc.queryForList("SELECT id FROM tb_raven_artist_image WHERE artist_id=? AND selected=FALSE",String.class,id).get(0);
        mvc.perform(put("/admin/artists/"+id+"/images/"+imageId+"/select").with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
        entityManager.flush();
        org.junit.jupiter.api.Assertions.assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM tb_raven_artist_image WHERE artist_id=? AND selected=TRUE",Integer.class,id));
        mvc.perform(multipart("/admin/artists/"+id+"/images").file(new MockMultipartFile("file","bad.png","image/png","not an image".getBytes()))
                .with(user("admin").roles("ADMIN"))).andExpect(status().isBadRequest());
    }

    @Test void adminCanPersistReplaceAndRemoveBackgroundWithoutChangingGallery() throws Exception {
        String id=createArtist();
        String base64="iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+aM1sAAAAASUVORK5CYII=";
        byte[] png=Base64.getDecoder().decode(base64);
        mvc.perform(multipart("/admin/artists/"+id+"/images")
                .file(new MockMultipartFile("file","photo.png","image/png",png)).with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        for (String background : List.of(base64, "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7")) {
            mvc.perform(multipart("/admin/artists/"+id+"/banner")
                    .file(new MockMultipartFile("file","background", "application/octet-stream",Base64.getDecoder().decode(background)))
                    .with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
            entityManager.flush(); entityManager.clear();
            mvc.perform(get("/artist/"+id).with(user("listener")))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.bannerImage").value(background))
                    .andExpect(jsonPath("$.artistImages.length()").value(1))
                    .andExpect(jsonPath("$.artistImages[0].selected").value(true))
                    .andExpect(jsonPath("$.artistImages[0].urlImage").value(base64));
        }
        mvc.perform(multipart("/admin/artists/"+id+"/banner")
                .file(new MockMultipartFile("file","fake.png","image/png","invalid".getBytes()))
                .with(user("admin").roles("ADMIN"))).andExpect(status().isBadRequest());
        mvc.perform(delete("/admin/artists/"+id+"/banner").with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
        entityManager.flush(); entityManager.clear();
        mvc.perform(get("/artist/"+id).with(user("listener")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.bannerImage").isEmpty())
                .andExpect(jsonPath("$.artistImages.length()").value(1));
    }

    @Test void featureFlagsEnforceServerBehaviorAndKeepAdminAccessible() throws Exception {
        String id=createArtist();
        mvc.perform(put("/admin/flags/artist_catalog").with(user("admin").roles("ADMIN"))
                .contentType("application/json").content("{\"enabled\":false}")).andExpect(status().isOk());
        mvc.perform(get("/artist").with(user("listener"))).andExpect(status().isServiceUnavailable());
        mvc.perform(get("/admin/artists").with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
        mvc.perform(put("/admin/flags/artist_photo_uploads").with(user("admin").roles("ADMIN"))
                .contentType("application/json").content("{\"enabled\":false}")).andExpect(status().isOk());
        mvc.perform(multipart("/admin/artists/"+id+"/images").file(new MockMultipartFile("file","image".getBytes()))
                .with(user("admin").roles("ADMIN"))).andExpect(status().isServiceUnavailable());
        mvc.perform(multipart("/admin/artists/"+id+"/banner").file(new MockMultipartFile("file","image".getBytes()))
                .with(user("admin").roles("ADMIN"))).andExpect(status().isServiceUnavailable());
        mvc.perform(put("/admin/flags/unknown").with(user("admin").roles("ADMIN"))
                .contentType("application/json").content("{\"enabled\":false}")).andExpect(status().isNotFound());
    }

    @Test void ordinaryUsersCannotAccessAdminOrLegacyArtistMutations() throws Exception {
        String bannerPath="/admin/artists/"+UUID.randomUUID()+"/banner";
        mvc.perform(multipart(bannerPath).file(new MockMultipartFile("file","image".getBytes()))
                .with(user("listener"))).andExpect(status().isForbidden());
        mvc.perform(delete(bannerPath).with(user("listener"))).andExpect(status().isForbidden());
        for(String path:List.of("/admin/artists","/admin/flags","/admin/logs","/admin/genres")) {
            mvc.perform(get(path).with(user("listener"))).andExpect(status().isForbidden());
            mvc.perform(get(path)).andExpect(status().isUnauthorized());
        }
        mvc.perform(post("/artist/register").with(user("listener"))).andExpect(status().isForbidden());
        mvc.perform(put("/artist/update/"+UUID.randomUUID()).with(user("listener"))).andExpect(status().isForbidden());
        mvc.perform(delete("/artist/"+UUID.randomUUID()).with(user("listener"))).andExpect(status().isForbidden());
        mvc.perform(put("/user/"+UUID.randomUUID()+"/email").with(user("listener"))
                .contentType("application/json").content("{\"email\":\"attacker@example.test\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/logs").with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
    }
}
