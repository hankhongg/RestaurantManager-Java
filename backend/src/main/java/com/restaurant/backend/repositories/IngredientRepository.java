package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
}
