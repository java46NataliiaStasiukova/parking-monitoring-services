package parking.monitoring.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import parking.monitoring.entities.Report;
import parking.monitoring.proj.ReportProjection;

public interface ReportRepository extends JpaRepository<Report, Long> {

	@Query(value="select id, cost, date, driver, name, status, zone, car_number as number from reports where date > :from and date < :to",
			nativeQuery = true)
	List<ReportProjection> getAllReportsByMonthAndYear(String from, String to);

	@Query(value="select * from reports where car_number = :carNumber",
			nativeQuery = true)
	List<Report> findReportByCarNumber(long carNumber);

	@Query(value = "select reports.id, cost, date, driver, reports.name, status, zone, car_number as number\n"
			+ "from reports join cars on reports.car_number = cars.number\n"
			+ "join drivers on cars.driver_id = drivers.id\n"
			+ "where drivers.birthdate > :from and drivers.birthdate < :to", 
			nativeQuery = true)
	List<ReportProjection> getReportsByAge(String from, String to);

	@Query(value = "select id, cost, date, driver, name, status, zone, car_number as number "
			+ "from reports where driver = :driverId", nativeQuery = true)
	List<ReportProjection> fingAllReportsByDriverId(long driverId);

	@Query(value = "select id, cost, date, driver, name, status, zone, car_number as number "
			+ "from reports where car_number = :carNumber", nativeQuery = true)
	List<ReportProjection> getReportsByCarNumber(long carNumber);

	@Query(value = "select id, cost, date, driver, name, status, zone, car_number as number "
			+ "from reports where status = 'canceled'", nativeQuery = true)
	List<ReportProjection> getCanceledReports();

	@Query(value = "select id, cost, date, driver, name, status, zone, car_number as number "
			+ "from reports where status = 'canceled' and car_number = :carNumber", nativeQuery = true)
	List<ReportProjection> getCanceledReportsByCarNumber(long carNumber);

	@Query(value = "select id, cost, date, driver, name, status, zone, car_number as number "
			+ "from reports where status = 'not-paid' and car_number = :carNumber", nativeQuery = true)
	List<ReportProjection> getNotPaidReportsByCarNumber(long carNumber);
	
	

}

