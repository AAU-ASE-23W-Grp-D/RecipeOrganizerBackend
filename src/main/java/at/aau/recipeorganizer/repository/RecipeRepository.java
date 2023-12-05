package at.aau.recipeorganizer.repository;

import at.aau.recipeorganizer.data.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}