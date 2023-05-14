package parking.monitoring.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import parking.monitoring.entities.Report;
import parking.monitoring.proj.ReportProjection;

public interface ReportRepository extends JpaRepository<Report, Long> {

	@Query(value="select * from reports where date > :from and date < :to",
			nativeQuery = true)
	List<ReportProjection> getAllReportsByMonthAndYear(String from, String to);

	@Query(value="select * from reports where car_number = :carNumber",
			nativeQuery = true)
	List<Report> findReportByCarNumber(long carNumber);
	
	//List<ReportProjection> getAllReportsByAge(String from, String to);

}

