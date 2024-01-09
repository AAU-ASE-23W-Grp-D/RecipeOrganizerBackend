package at.aau.recipeorganizer.service;

import at.aau.recipeorganizer.data.Recipe;
import at.aau.recipeorganizer.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void testUpdateRecipe() {
        Recipe existingRecipe = new Recipe("Existing Test Recipe",  "Existing Ingredient", "Existing Test Description");
        Recipe updatedRecipe = new Recipe("Updated Test Recipe", "Updated Ingredient", "Updated Test Description");
        long recipeId = existingRecipe.id;

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(existingRecipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(updatedRecipe);

        Optional<Recipe> result = recipeService.update(updatedRecipe, recipeId);

        assertTrue(result.isPresent());
        assertEquals(updatedRecipe, result.get());

        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, times(1)).save(existingRecipe.update(updatedRecipe));
    }

    @Test
    void testFindAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient 1", "Test Description 1");
        Recipe recipe2 = new Recipe("Test Recipe 2",  "Test Ingredient 2", "Test Description 2");
        Recipe recipe3 = new Recipe("Test Recipe 3",  "Test Ingredient 3", "Test Description 3");

        list.add(recipe1);
        list.add(recipe2);
        list.add(recipe3);

        when(recipeRepository.findAll()).thenReturn(list);

        List<Recipe> empList = recipeService.findAll();

        assertEquals(3, empList.size());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void testSaveRecipe() {
        Recipe recipe = new Recipe("Test Recipe", "Test Ingredient", "Test Description");

        recipeService.save(recipe);

        verify(recipeRepository, times(1)).save(recipe);
    }

    @Test
    void testFindByIdRecipe() {
        Recipe recipe = new Recipe("Test Recipe", "Test Ingredient", "Test Description");
        long recipeId = recipe.id;

        when(recipeRepository.findById(recipe.id)).thenReturn(Optional.of(recipe));

        Optional<Recipe> result = recipeService.findById(recipeId);

        assertTrue(result.isPresent());
        assertEquals(recipe, result.get());

        verify(recipeRepository, times(1)).findById(recipeId);
    }

    @Test
    void deleteByIdRecipe() {
        long recipeId = 1;

        recipeService.deleteById(recipeId);

        verify(recipeRepository, times(1)).deleteById(recipeId);
    }
}
