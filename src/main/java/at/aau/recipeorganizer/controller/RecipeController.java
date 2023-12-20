package at.aau.recipeorganizer.controller;

import at.aau.recipeorganizer.data.Recipe;
import at.aau.recipeorganizer.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Recipe> save(@RequestBody Recipe recipe) {
        return ResponseEntity.ok(service.save(recipe));
    }

    @GetMapping("{id}")
    public ResponseEntity<Recipe> findById(@PathVariable long id) {
        return ResponseEntity.of(service.findById(id));
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id) {
        service.deleteById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<Recipe> update(@PathVariable long id, @RequestBody Recipe recipe) {
        return ResponseEntity.of(service.update(recipe, id));
    }
}
