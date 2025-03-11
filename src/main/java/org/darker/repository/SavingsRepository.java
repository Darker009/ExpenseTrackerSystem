package org.darker.repository;

import org.darker.entity.Savings;
import org.darker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface SavingsRepository extends JpaRepository<Savings, Long> {

	Optional<Savings> findByUser(User user); // ✅ Return Optional<Savings>

	@Query("SELECT COALESCE(SUM(s.totalBalance), 0) FROM Savings s " + "WHERE s.user = :user "
			+ "AND YEAR(s.date) = :year " + "AND MONTH(s.date) = :month")
	BigDecimal findTotalIncomeByUserAndMonth(@Param("user") User user, @Param("year") int year,
			@Param("month") int month);

	boolean existsByUser(User user);
}
