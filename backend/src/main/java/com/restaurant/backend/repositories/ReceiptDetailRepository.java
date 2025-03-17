package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Integer> {
}
