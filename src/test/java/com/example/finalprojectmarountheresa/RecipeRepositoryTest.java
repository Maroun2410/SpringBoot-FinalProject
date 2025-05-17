package com.example.finalprojectmarountheresa;

import com.example.finalprojectmarountheresa.model.Recipe;
import com.example.finalprojectmarountheresa.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();

        Recipe recipe = new Recipe();
        recipe.setTitle("Test Pasta");
        recipe.setIngredients(List.of("pasta", "cheese"));
        recipe.setInstructions("Boil and mix");
        recipe.setCookingTime(10);
        recipe.setCategory("Main Dish");

        recipeRepository.save(recipe);
    }

    @Test
    void shouldFindByCategoryIgnoreCase() {
        List<Recipe> result = recipeRepository.findByCategoryIgnoreCase("main dish");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Pasta");
    }

    @Test
    void shouldFindByTitleContainingIgnoreCase() {
        List<Recipe> result = recipeRepository.findByTitleContainingIgnoreCase("pasta");
        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldFindByCookingTimeLessThanEqual() {
        List<Recipe> result = recipeRepository.findByCookingTimeLessThanEqual(15);
        assertThat(result).hasSize(1);
    }
}
