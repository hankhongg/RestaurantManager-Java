package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.Stockin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockinRepository extends JpaRepository<Stockin, Integer> {
}
