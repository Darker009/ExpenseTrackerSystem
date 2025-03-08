package org.darker.service;

import jakarta.transaction.Transactional;
import org.darker.dto.MonthlyReportDTO;
import org.darker.entity.MonthlyReport;
import org.darker.entity.User;
import org.darker.exception.ResourceNotFoundException;
import org.darker.repository.MonthlyReportRepository;
import org.darker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
@Transactional
public class MonthlyReportService {
	private final MonthlyReportRepository reportRepository;
	private final UserRepository userRepository;

	public MonthlyReportService(MonthlyReportRepository reportRepository, UserRepository userRepository) {
		this.reportRepository = reportRepository;
		this.userRepository = userRepository;
	}

	public MonthlyReportDTO getMonthlyReport(Long userId, YearMonth month) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		MonthlyReport report = reportRepository.findByUserAndMonth(user, month)
				.orElseThrow(() -> new ResourceNotFoundException("Report not found"));

		return new MonthlyReportDTO(report.getId(), user.getId(), report.getMonth(), report.getTotalIncome(),
				report.getTotalExpense(), report.getRemainingBalance());
	}
}
