package parking.monitoring.service;

import java.util.List;

import parking.monitoring.dto.*;
import parking.monitoring.entities.*;
import parking.monitoring.proj.*;


public interface BackOfficeService {

	void addDriver(DriverDto driver);

	void addCar(CarDto car);

	void addReport(ReportDto report);

	void updateDriver(long driverId, String email);

	void updateCar(long carNumber, long driverId);

	void updateReport(long reportId, String status);

	Driver getDriver(long driverId);

	Car getCar(long carId);

	Report getReport(long reportId);

	Report deleteReport(long reportId);
	
	Car deleteCar(long carNumber);
	
	Driver deleteDriver(long driverId);

	List<ReportProjection> getAllReportsByMonthYear(int year, String month);

	//List<ReportProjection> getAllReportsByDriverAge(int age);

}
