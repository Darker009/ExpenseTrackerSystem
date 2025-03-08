package org.darker.controller;

import org.darker.dto.MonthlyReportDTO;
import org.darker.service.MonthlyReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/reports")
public class MonthlyReportController {
	private final MonthlyReportService reportService;

	public MonthlyReportController(MonthlyReportService reportService) {
		this.reportService = reportService;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<MonthlyReportDTO> getMonthlyReport(@PathVariable Long userId, @RequestParam String month) {
		return ResponseEntity.ok(reportService.getMonthlyReport(userId, YearMonth.parse(month)));
	}
}
