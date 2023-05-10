package parking.monitoring.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import parking.monitoring.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

//	@Query(value="select * from reports where date > :from and date < :to",
//			nativeQuery = true)
//	List<ReportProjection> getAllReportsByMonthAndYear(String from, String to);

}

