package parking.monitoring.service;

import parking.monitoring.dto.*;
import parking.monitoring.dto.DriverDto;
import parking.monitoring.dto.ReportDto;


public interface BackOfficeService {

	void addDriver(DriverDto driver);

	void addCar(CarDto car);

	void addReport(ReportDto report);

//	void updateDriver(long driverId, String email);
//
//	void updateCar(long carNumber, long driverId);
//
//	void updateReport(long reportId, String status);
//
//	Driver getDriver(long driverId);
//
//	Car getCar(long carId);
//
//	Report getReport(long reportId);
//
//	Report deleteReport(long reportId);
//
//	List<ReportProjection> getAllReportsByMonthYear(int year, String month);
//
//	List<Report> getAllReportsByDriverAge(int age);

}
