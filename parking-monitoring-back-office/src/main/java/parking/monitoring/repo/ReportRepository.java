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

//	@Query(value = "select reports.id, cost, date, driver_number, reports.name, status, zone, reports.car_number\n"
//			+ "from reports join cars on reports.car_number = cars.car_number\n"
//			+ "join drivers on cars.driver_id = drivers.id\n"
//			+ "where drivers.birthdate > ':from-12-31' and drivers.birthdate < ':to-01-01'", 
//			nativeQuery = true)
//	List<ReportProjection> getReportsByAge(int from, int to);
	
	

}

