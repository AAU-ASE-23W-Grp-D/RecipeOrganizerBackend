package at.aau.recipeorganizer.service;

import at.aau.recipeorganizer.data.Recipe;
import at.aau.recipeorganizer.service.RecipeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeService repository;

    public RecipeService(RecipeService repository) {
        this.repository = repository;
    }

    public Optional<Recipe> findById(Long id) {
        return repository.findById(id);
    }

    public List<Recipe> findAll() {
        var allRecipes = new ArrayList<Recipe>();
        for (var recipe : repository.findAll())
            allRecipes.add(recipe);
        return allRecipes;
    }

    public Recipe save(Recipe recipe) {
        return repository.save(recipe);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<Recipe> update(Recipe recipe, long id) {
        return repository.findById(id)
                .map(r -> repository.save(r.update(recipe)));
    }
}
