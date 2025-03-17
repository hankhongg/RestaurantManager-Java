package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.FinancialHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialHistoryRepository extends JpaRepository<FinancialHistory, Integer> {
}
