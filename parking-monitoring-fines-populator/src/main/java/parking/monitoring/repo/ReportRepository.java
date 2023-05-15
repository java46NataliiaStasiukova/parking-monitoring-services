package parking.monitoring.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import parking.monitoring.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
