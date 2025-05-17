package com.example.finalprojectmarountheresa.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "recipes")
public class Recipe {

    @Id
    private String id;
    @NotBlank(message = "Title is required")
    private String title;

    @Size(min = 1, message = "At least one ingredient is required")
    private List<String> ingredients;

    @NotBlank(message = "Instructions are required")
    private String instructions;

    @Min(value = 1, message = "Cooking time must be at least 1 minute")
    private int cookingTime;

    @NotBlank(message = "Category is required")
    private String category;


    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public int getCookingTime() { return cookingTime; }
    public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
