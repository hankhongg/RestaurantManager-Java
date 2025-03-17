package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Integer> {
}
