package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
}
