package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
