package at.aau.recipeorganizer;

import at.aau.recipeorganizer.data.Recipe;
import at.aau.recipeorganizer.data.Role;
import at.aau.recipeorganizer.data.User;
import at.aau.recipeorganizer.repository.RecipeRepository;
import at.aau.recipeorganizer.repository.RoleRepository;
import at.aau.recipeorganizer.repository.UserRepository;
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
    CommandLineRunner initDatabase(UserRepository users, RoleRepository roles, RecipeRepository recipes) {
        return args -> {
            // TODO remove this
            // these are just some default values
            roles.save(new Role(Role.ERole.ROLE_ADMIN));
            roles.save(new Role(Role.ERole.ROLE_USER));

            User admin = new User("admin", "admin@email.com", "$2a$12$d.dFoMghFSDjhu9d8uupHuU0Qx2FWikldBrGa4yuXz68YEPk/sWjm");
            admin.roles.add(new Role(Role.ERole.ROLE_ADMIN));
            User testUser = new User("testUser", "test@email.com", "$2a$12$v9ykV0/PH0EOAC12pfqWlu4YzsykY8u0TLcd1hnex0I0oGES.htoO");
            testUser.roles.add(new Role(Role.ERole.ROLE_USER));
            users.save(admin);
            users.save(testUser);

            recipes.save(new Recipe("name1", "desc1"));
            recipes.save(new Recipe("name2", "desc2"));
            recipes.save(new Recipe("name3", "desc3"));
        };
    }
}
