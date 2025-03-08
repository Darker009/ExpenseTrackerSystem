package org.darker.repository;

import org.darker.entity.Savings;
import org.darker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SavingsRepository extends JpaRepository<Savings, Long> {

    // ✅ Find Savings by User (more efficient by fetching the full User object)
    Optional<Savings> findByUser(User user); // This is the correct method

    // ✅ New method to find Savings by userId
    Optional<Savings> findByUserId(Long userId);

    // ✅ Check if Savings account exists for a user by User
    boolean existsByUser(User user);
}
