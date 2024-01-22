package at.aau.recipeorganizer.controller;

import at.aau.recipeorganizer.data.Recipe;
import at.aau.recipeorganizer.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class RecipeControllerTest {
    private static final String BASE_URL = "/api/recipes";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    private ObjectMapper objectMapper;

    private Recipe recipe;

    private final byte[] image = new byte[]{0x01};

    @BeforeEach
    public void init() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        recipe = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
    }

    @Test
    void testFindById() throws Exception {
        when(recipeService.findById(0L)).thenReturn(Optional.of(recipe));

        mockMvc.perform(get(BASE_URL + "/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Recipe"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(recipeService, times(1)).findById(0L);
    }

    @Test
    void testSave() throws Exception {
        when(recipeService.save(recipe)).thenReturn(recipe);

        mockMvc.perform(post(BASE_URL)
                        .content(objectMapper.writeValueAsString(recipe))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Recipe"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(recipeService, times(1)).save(recipe);
    }

    @Test
    void testUpdate() throws Exception {
        when(recipeService.update(recipe, 0L)).thenReturn(Optional.of(recipe));

        mockMvc.perform(put(BASE_URL + "/0")
                        .content(objectMapper.writeValueAsString(recipe))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Recipe"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(recipeService, times(1)).update(recipe, 0L);
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(recipeService, times(1)).deleteById(1L);
    }

    @Test
    void testFindAll() throws Exception {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe);
        when(recipeService.findAll()).thenReturn(recipes);

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[*].name", hasItems("Test Recipe")))
                .andExpect(jsonPath("$.[*].description", hasItems("Test Description")));

        verify(recipeService, times(1)).findAll();
    }
}