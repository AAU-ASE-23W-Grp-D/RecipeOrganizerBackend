package at.aau.recipeorganizer;

import at.aau.recipeorganizer.data.Recipe;
import at.aau.recipeorganizer.data.Role;
import at.aau.recipeorganizer.repository.RecipeRepository;
import at.aau.recipeorganizer.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RecipeOrganizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecipeOrganizerApplication.class);
    }

    @Bean
    CommandLineRunner initDatabase(RecipeRepository recipes, RoleRepository roles) {
        return args -> {
            // TODO remove this
            // these are just some default values
            recipes.save(new Recipe("name1", "desc1"));
            recipes.save(new Recipe("name2", "desc2"));
            recipes.save(new Recipe("name3", "desc3"));

            roles.save(new Role(Role.ERole.ROLE_ADMIN));
            roles.save(new Role(Role.ERole.ROLE_USER));
        };
    }

}
