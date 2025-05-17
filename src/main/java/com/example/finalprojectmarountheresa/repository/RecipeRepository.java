package com.example.finalprojectmarountheresa.repository;

import com.example.finalprojectmarountheresa.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {

    List<Recipe> findByCategoryIgnoreCase(String category);

    @Query("{ 'ingredients': { $in: [?0] } }")
    List<Recipe> findByIngredient(String ingredient);

    List<Recipe> findByTitleContainingIgnoreCase(String title);

    List<Recipe> findByCookingTimeLessThanEqual(Integer maxTime);

}

