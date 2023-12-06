package at.aau.recipeorganizer.controller;

import at.aau.recipeorganizer.data.Recipe;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void findAll() {
    }

    @Test
    void save() throws Exception {
        var recipe = new Recipe("foobar", "baz");
        var request = post("/api/recipes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(recipe));
        var result = mvc.perform(request).andExpect(status().isOk()).andReturn();
        var actual = objectMapper.readValue(result.getResponse().getContentAsString(), Recipe.class);
        actual.id = 0; // ignore id
        assertEquals(recipe, actual);
    }

    @Test
    void findById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void update() {
    }
}