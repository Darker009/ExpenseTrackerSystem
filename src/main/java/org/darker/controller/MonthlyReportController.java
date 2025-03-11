package org.darker.controller;

import org.darker.dto.MonthlyReportDTO;
import org.darker.service.MonthlyReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/reports")
public class MonthlyReportController {

	private final MonthlyReportService reportService;

	public MonthlyReportController(MonthlyReportService reportService) {
		this.reportService = reportService;
	}

	@GetMapping("/{userId}") 
	public ResponseEntity<?> getMonthlyReport(@PathVariable Long userId, @RequestParam String month) {
		if (month == null || month.isBlank()) {
			return ResponseEntity.badRequest().body("Month parameter is required.");
		}
		try {
			LocalDate monthStart = YearMonth.parse(month).atDay(1);
			MonthlyReportDTO report = reportService.getMonthlyReport(userId, monthStart); 
			return ResponseEntity.ok(report);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Invalid month format. Use 'YYYY-MM' (e.g., 2025-03).");
		}
	}
}
