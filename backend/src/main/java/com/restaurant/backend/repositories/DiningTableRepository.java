package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiningTableRepository extends JpaRepository<DiningTable, Integer> {
}
