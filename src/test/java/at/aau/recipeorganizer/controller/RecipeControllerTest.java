package at.aau.recipeorganizer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RecipeControllerTest {
    private static final String BASE_URL = "/api/receipe";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getReceipeRequestIsAnonymous_thenUnauthorized() throws Exception {
        mockMvc.perform(get(BASE_URL).with(anonymous()))
                .andExpect(status().isUnauthorized());
    }
}