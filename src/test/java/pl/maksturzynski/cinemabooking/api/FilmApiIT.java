package pl.maksturzynski.cinemabooking.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.maksturzynski.cinemabooking.repository.FilmRepository;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FilmApiIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired FilmRepository filmRepository;

    @BeforeEach
    void clean() {
        filmRepository.deleteAll();
    }

    @Test
    void should_create_list_get_update_delete_film() throws Exception {
        // LIST empty
        mvc.perform(get("/api/v1/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));

        // CREATE
        var createJson = """
                {
                  "title": "Test Film",
                  "genre": "Action",
                  "ageRating": 13,
                  "director": "John Doe",
                  "castText": "A, B, C",
                  "trailerUrl": "https://example.com/trailer"
                }
                """;

        var createRes = mvc.perform(post("/api/v1/films")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/v1/films/")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Test Film")))
                .andReturn();

        long id = om.readTree(createRes.getResponse().getContentAsString()).get("id").asLong();

        // LIST non-empty
        mvc.perform(get("/api/v1/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is((int) id)));

        // GET by id
        mvc.perform(get("/api/v1/films/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.title", is("Test Film")));

        // UPDATE
        var updateJson = """
                {
                  "title": "Updated Film",
                  "genre": "Drama",
                  "ageRating": 18,
                  "director": "Jane Doe",
                  "castText": "X, Y",
                  "trailerUrl": "https://example.com/new"
                }
                """;

        mvc.perform(put("/api/v1/films/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Film")))
                .andExpect(jsonPath("$.ageRating", is(18)));

        // DELETE
        mvc.perform(delete("/api/v1/films/{id}", id).with(csrf()))
                .andExpect(status().isNoContent());

        // GET -> 404
        mvc.perform(get("/api/v1/films/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_404_for_missing_film() throws Exception {
        mvc.perform(get("/api/v1/films/{id}", 999999))
                .andExpect(status().isNotFound());
    }
}
