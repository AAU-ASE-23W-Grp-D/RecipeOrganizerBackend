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
            User testUser2 = new User("testUser2", "test2@email.com", "$2a$12$v9ykV0/PH0EOAC12pfqWlu4YzsykY8u0TLcd1hnex0I0oGES.htoO");

            Recipe recipe1 = new Recipe("Pizza", "Teig, Tomaten", "Beschreibung Pizza");
            Recipe recipe2= new Recipe("Pasta", "Nudel, Tomaten", "Beschreibung Pasta");
            Recipe recipe3 = new Recipe("Brot", "Teig", "Beschreibung Brot");

            testUser2.setOwnRecipes(recipe1);
            testUser2.setOwnRecipes(recipe2);
            testUser2.setLikedRecipes(recipe3);
            users.save(testUser2);

            recipes.save(recipe1);
            recipes.save(recipe2);
            recipes.save(recipe3);
        };
    }
}
