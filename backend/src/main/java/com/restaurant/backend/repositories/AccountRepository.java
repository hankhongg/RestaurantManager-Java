package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
