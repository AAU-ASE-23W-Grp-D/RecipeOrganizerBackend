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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class RecipeOrganizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecipeOrganizerApplication.class);
    }

    String imagePath = System.getProperty("user.dir") + "/src/main/resources/images/Pasta.jpg";

    Path path = Paths.get(imagePath);

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
            User testUser2 = new User("testUser2", "test2@email.com", "$2a$12$v9ykV0/PH0EOAC12pfqWlu4YzsykY8u0TLcd1hnex0I0oGES.htoO");
            User testUser3 = new User("testUser3", "test3@email.com", "$2a$12$v9ykV0/PH0EOAC12pfqWlu4YzsykY8u0TLcd1hnex0I0oGES.htoO");
            User testUser4 = new User("testUser4", "test4@email.com", "$2a$12$v9ykV0/PH0EOAC12pfqWlu4YzsykY8u0TLcd1hnex0I0oGES.htoO");

            Recipe recipe1 = new Recipe("Pizza", "Teig, Tomaten", "Beschreibung Pizza", 5, Files.readAllBytes(path));
            Recipe recipe2= new Recipe("Pasta", "Nudel, Tomaten", "Beschreibung Pasta", 5, Files.readAllBytes(path));
            Recipe recipe3 = new Recipe("Brot", "Teig", "Beschreibung Brot", 5, Files.readAllBytes(path));
            Recipe recipe4 = new Recipe("Pizza", "Teig, Tomaten", "Beschreibung Pizza", 5, Files.readAllBytes(path));
            Recipe recipe5 = new Recipe("Pasta", "Nudel, Tomaten", "Beschreibung Pasta", 5, Files.readAllBytes(path));
            Recipe recipe6 = new Recipe("Brot", "Teig", "Beschreibung Brot", 5, Files.readAllBytes(path));

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
