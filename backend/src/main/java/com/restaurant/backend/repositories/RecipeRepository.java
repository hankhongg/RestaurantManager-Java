package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}
