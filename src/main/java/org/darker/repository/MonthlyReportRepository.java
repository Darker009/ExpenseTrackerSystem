package org.darker.repository;

import org.darker.entity.MonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.YearMonth;
import java.util.Optional;

public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, Long> {
    Optional<MonthlyReport> findByUserIdAndMonth(Long userId, YearMonth month);
}
