package com.example.finalprojectmarountheresa.controller;

import com.example.finalprojectmarountheresa.model.Recipe;
import com.example.finalprojectmarountheresa.repository.RecipeRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @PostMapping
    public Recipe createRecipe(@Valid @RequestBody Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Recipe>> getPaginatedRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sortOrder = direction.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Recipe> result = recipeRepository.findAll(pageable);

        return ResponseEntity.ok(result);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        return recipeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable String id, @Valid @RequestBody Recipe updated) {
        return recipeRepository.findById(id).map(existing -> {
            Recipe recipe = existing;
            recipe.setTitle(updated.getTitle());
            recipe.setIngredients(updated.getIngredients());
            recipe.setInstructions(updated.getInstructions());
            recipe.setCookingTime(updated.getCookingTime());
            recipe.setCategory(updated.getCategory());
            return ResponseEntity.ok(recipeRepository.save(recipe));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<Recipe> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String ingredient,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer maxTime) {

        if (category != null) return recipeRepository.findByCategoryIgnoreCase(category);
        if (ingredient != null) return recipeRepository.findByIngredient(ingredient);
        if (title != null) return recipeRepository.findByTitleContainingIgnoreCase(title);
        if (maxTime != null) return recipeRepository.findByCookingTimeLessThanEqual(maxTime);

        return recipeRepository.findAll();
    }

}
