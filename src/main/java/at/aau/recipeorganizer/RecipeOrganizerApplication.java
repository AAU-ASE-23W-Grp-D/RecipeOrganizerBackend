package at.aau.recipeorganizer;

import at.aau.recipeorganizer.data.Recipe;
import at.aau.recipeorganizer.data.User;
import at.aau.recipeorganizer.repository.RecipeRepository;
import at.aau.recipeorganizer.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class RecipeOrganizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecipeOrganizerApplication.class);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .maxAge(3600);
            }
        };
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository users, RecipeRepository recipes) {
        return args -> {
            // TODO remove this
            // these are just some default values
            // If there are already some recipes then don't create the default values again
            if (recipes.count() > 0) {
                return;
            }

            User testUser2 = new User("testUser2", "test2@email.com", "$2a$12$d.dFoMghFSDjhu9d8uupHuU0Qx2FWikldBrGa4yuXz68YEPk/sWjm");
            User testUser3 = new User("testUser3", "test3@email.com", "$2a$12$d.dFoMghFSDjhu9d8uupHuU0Qx2FWikldBrGa4yuXz68YEPk/sWjm");
            User testUser4 = new User("testUser4", "test4@email.com", "$2a$12$d.dFoMghFSDjhu9d8uupHuU0Qx2FWikldBrGa4yuXz68YEPk/sWjm");

            byte[] file = new byte[0];
            URL url = getClass().getResource("/images/Pasta.jpg");
            if (url != null) {
                Path path = Paths.get(url.toURI());
                file = Files.readAllBytes(path);
            }
            Recipe recipe1 = new Recipe("Pizza", "100g*Teig,200g*Tomaten", "Beschreibung Pizza", 2, 1, file);
            Recipe recipe2= new Recipe("Pasta", "100g*Nudel,200g*Tomaten", "Beschreibung Pasta", 4, 1, file);
            Recipe recipe3 = new Recipe("Brot", "1kg*Teig", "Beschreibung Brot", 3, 1, file);
            Recipe recipe4 = new Recipe("Pizza", "100g*Teig,1kg*Tomaten", "Beschreibung Pizza", 1, 1, file);
            Recipe recipe5 = new Recipe("Pasta", "1000g*Nudel,100g*Tomaten", "Beschreibung Pasta", 4, 1, file);
            Recipe recipe6 = new Recipe("Brot", "200g*Teig", "Beschreibung Brot", 3, 1, file);

            testUser2.addOwnRecipe(recipe1);
            testUser2.addOwnRecipe(recipe2);
            testUser3.addOwnRecipe(recipe3);
            testUser3.addOwnRecipe(recipe4);
            testUser3.addOwnRecipe(recipe5);
            testUser3.addOwnRecipe(recipe6);
            testUser2.removeOwnRecipe(recipe2);
            users.save(testUser2);
            users.save(testUser3);
            users.save(testUser4);

            recipes.save(recipe1);
            recipes.save(recipe2);
            recipes.save(recipe3);
            recipes.save(recipe4);
            recipes.save(recipe5);
            recipes.save(recipe6);

            testUser3.addLikedRecipe(recipe1);
            testUser4.addLikedRecipe(recipe1);
            testUser4.addLikedRecipe(recipe3);

            testUser4.removeLikedRecipe(recipe1);
            users.save(testUser3);
            users.save(testUser4);
        };
    }
}
