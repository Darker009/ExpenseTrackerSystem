package org.darker.service;

import jakarta.transaction.Transactional;
import org.darker.dto.MonthlyReportDTO;
import org.darker.entity.Expense;
import org.darker.entity.MonthlyReport;
import org.darker.entity.User;
import org.darker.exception.ResourceNotFoundException;
import org.darker.repository.MonthlyReportRepository;
import org.darker.repository.UserRepository;
import org.darker.repository.ExpenseRepository;
import org.darker.repository.SavingsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class MonthlyReportService {

	private final MonthlyReportRepository reportRepository;
	private final UserRepository userRepository;
	private final ExpenseRepository expenseRepository;
	private final SavingsRepository savingsRepository;

	public MonthlyReportService(MonthlyReportRepository reportRepository, UserRepository userRepository,
			ExpenseRepository expenseRepository, SavingsRepository savingsRepository) {
		this.reportRepository = reportRepository;
		this.userRepository = userRepository;
		this.expenseRepository = expenseRepository;
		this.savingsRepository = savingsRepository;
	}

	public MonthlyReportDTO getMonthlyReport(Long userId, LocalDate monthStart) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		MonthlyReport report = reportRepository.findByUserAndMonth(user, monthStart).orElse(null);

		// Compute start and end dates for the month
		LocalDate startDate = monthStart;
		LocalDate endDate = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

		// Get all expenses in that period
		List<Expense> expenses = expenseRepository.findByUserAndDateBetween(user, startDate, endDate);

		// Group expenses by category name and sum their amounts
		Map<String, BigDecimal> expenseBreakdown = expenses.stream()
				.collect(Collectors.groupingBy(expense -> expense.getCategory().name(),
						Collectors.mapping(Expense::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

		YearMonth yearMonth = YearMonth.from(monthStart);
		int year = yearMonth.getYear();
		int monthValue = yearMonth.getMonthValue();

		BigDecimal totalExpense = expenseRepository.findTotalExpenseByUserAndMonth(user, year, monthValue);
		BigDecimal totalIncome = savingsRepository.findTotalIncomeByUserAndMonth(user, year, monthValue);
		BigDecimal remainingBalance = totalIncome.subtract(totalExpense);

		// If no report exists, create one and save it
		if (report == null) {
			report = new MonthlyReport(user, monthStart, totalIncome, totalExpense, remainingBalance);
			reportRepository.save(report);
		}

		// Return DTO including the expense breakdown
		return new MonthlyReportDTO(report.getId(), user.getId(), report.getMonth(), report.getTotalIncome(),
				report.getTotalExpense(), report.getRemainingBalance(), expenseBreakdown);
	}
}
