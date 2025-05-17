package com.example.finalprojectmarountheresa;

import com.example.finalprojectmarountheresa.model.Recipe;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.finalprojectmarountheresa.controller.RecipeController;
import com.example.finalprojectmarountheresa.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeRepository recipeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setTitle("Mocked Pizza");
        recipe.setIngredients(List.of("dough", "cheese"));
        recipe.setInstructions("Bake");
        recipe.setCookingTime(15);
        recipe.setCategory("Main");

        when(recipeRepository.save(any())).thenReturn(recipe);

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetRecipeById_NotFound() throws Exception {
        when(recipeRepository.findById("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/recipes/nonexistent"))
                .andExpect(status().isNotFound());
    }
}
