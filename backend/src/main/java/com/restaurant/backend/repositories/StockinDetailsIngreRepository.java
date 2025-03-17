package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.StockinDetailsIngre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockinDetailsIngreRepository extends JpaRepository<StockinDetailsIngre, Integer> {
}
