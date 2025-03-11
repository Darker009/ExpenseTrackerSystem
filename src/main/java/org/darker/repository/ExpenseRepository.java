package org.darker.repository;

import org.darker.entity.Expense;
import org.darker.entity.User;
import org.darker.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	List<Expense> findByUser(User user);

	List<Expense> findByUserAndCategory(User user, ExpenseCategory category);

	@Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " + "WHERE e.user = :user "
			+ "AND FUNCTION('YEAR', e.date) = :year " + "AND FUNCTION('MONTH', e.date) = :month")
	BigDecimal findTotalExpenseByUserAndMonth(@Param("user") User user, @Param("year") int year,
			@Param("month") int month);

	List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
