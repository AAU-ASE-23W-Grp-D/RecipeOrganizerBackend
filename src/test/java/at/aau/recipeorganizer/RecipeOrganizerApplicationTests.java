package at.aau.recipeorganizer;

import at.aau.recipeorganizer.data.Recipe;
import at.aau.recipeorganizer.repository.RecipeRepository;
import at.aau.recipeorganizer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class RecipeOrganizerApplicationTests {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RecipeRepository recipeRepository;

    @Test
    void contextLoads() {
        // Ensure that the application context loads successfully
        assert(true);
    }

    @Test
    void testDefaultRecipesAreLoaded() throws Exception {
        CommandLineRunner initDatabase = new RecipeOrganizerApplication().initDatabase(userRepository, recipeRepository);
        initDatabase.run();

        Mockito.verify(recipeRepository, Mockito.times(6)).save(Mockito.any(Recipe.class));
    }
}
