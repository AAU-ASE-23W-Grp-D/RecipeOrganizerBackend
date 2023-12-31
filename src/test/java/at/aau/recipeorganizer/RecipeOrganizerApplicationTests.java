package at.aau.recipeorganizer;

import at.aau.recipeorganizer.data.Recipe;
import at.aau.recipeorganizer.repository.RecipeRepository;
import at.aau.recipeorganizer.repository.RoleRepository;
import at.aau.recipeorganizer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

@SpringBootTest
class RecipeOrganizerApplicationTests {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private RecipeRepository recipeRepository;

    @Test
    void contextLoads() {
        // Ensure that the application context loads successfully
    }

    @Test
    void testDefaultRecipesAreLoaded() throws Exception {
        Recipe recipe1 = new Recipe("name1", "desc1");
        Recipe recipe2 = new Recipe("name2", "desc2");
        Recipe recipe3 = new Recipe("name3", "desc3");

        Mockito.when(recipeRepository.findAll()).thenReturn(Arrays.asList(recipe1, recipe2, recipe3));

        CommandLineRunner initDatabase = new RecipeOrganizerApplication().initDatabase(userRepository, roleRepository, recipeRepository);
        initDatabase.run();

        Mockito.verify(recipeRepository, Mockito.times(3)).save(Mockito.any(Recipe.class));
    }
}
