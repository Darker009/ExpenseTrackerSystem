package org.darker.repository;

import org.darker.entity.Expense;
import org.darker.entity.User;
import org.darker.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
    List<Expense> findByUserAndCategory(User user, ExpenseCategory category);
    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
