package org.darker.repository;

import org.darker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);  // Get all expenses by user
    List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end); // For reports
}
