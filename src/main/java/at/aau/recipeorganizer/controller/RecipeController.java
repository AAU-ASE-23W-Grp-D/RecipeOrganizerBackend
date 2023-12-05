package at.aau.recipeorganizer.controller;

import at.aau.recipeorganizer.data.Recipe;
import at.aau.recipeorganizer.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Recipe> findAll() {
        return service.findAll();
    }

    @PostMapping
    public Recipe save(@RequestBody Recipe recipe) {
        return service.save(recipe);
    }

    @GetMapping("{id}")
    public ResponseEntity<Recipe> findById(@PathVariable long id) {
        var recipe = service.findById(id);
        if (recipe.isPresent()) return ResponseEntity.ok(recipe.get());


        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found");
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id) {
        service.deleteById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<Recipe> update(@PathVariable long id, @RequestBody Recipe recipe) {
        var updatedRecipe = service.update(recipe, id);
        if (updatedRecipe.isPresent()) return ResponseEntity.ok(updatedRecipe.get());

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found");
    }
}
