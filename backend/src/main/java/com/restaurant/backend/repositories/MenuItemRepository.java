package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}
