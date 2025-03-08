package org.darker.repository;

import org.darker.entity.Savings;
import org.darker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SavingsRepository extends JpaRepository<Savings, Long> {

	Optional<Savings> findByUser(User user);

	Optional<Savings> findByUserId(Long userId);

	boolean existsByUser(User user);
}
