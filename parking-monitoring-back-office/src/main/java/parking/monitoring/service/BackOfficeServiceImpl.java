package parking.monitoring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import parking.monitoring.dto.CarDto;
import parking.monitoring.dto.DriverDto;
import parking.monitoring.dto.ReportDto;
import parking.monitoring.entities.Car;
import parking.monitoring.entities.Driver;
import parking.monitoring.entities.Report;
import parking.monitoring.repo.CarRepository;
import parking.monitoring.repo.DriverRepository;
import parking.monitoring.repo.ReportRepository;

@Service
@Transactional(readOnly = true)
public class BackOfficeServiceImpl implements BackOfficeService {

	static Logger LOG = LoggerFactory.getLogger(BackOfficeServiceImpl.class);
	@Autowired
	CarRepository carRepository;
	@Autowired
	DriverRepository driverRepository;
	@Autowired
	ReportRepository reportRepository;

	@Override
	@Transactional
	public void addDriver(DriverDto driver) {
		if(driverRepository.existsById(driver.id)) {
			throw new IllegalStateException(String.format("Driver with id: %s already exist",
					driver.id));
		}
		driverRepository.save(new Driver(driver.id, driver.name, driver.email, driver.birthdate));
	}

	@Override
	@Transactional
	public void addCar(CarDto car) {
		if(carRepository.existsById(car.carNumber)) {
			throw new IllegalStateException(String.format("Car with number: %s already exist",
					car.carNumber));
		}
		Driver driver = driverRepository.findById(car.driverId).orElse(null);
		if(driver == null) {
			throw new IllegalStateException(String.format("Driver with id: %s doesn't exist",
					car.driverId));
		}
		carRepository.save(new Car(car.carNumber, driver));
	}

	@Override
	@Transactional
	public void addReport(ReportDto report) {
		Car car = carRepository.findById(report.carNumber).orElse(null);
		if(car == null) {
			throw new IllegalStateException(String.format("Car with number: %s doesn't exist",
					report.carNumber));
		}
		Driver driver = driverRepository.findById(report.driverNumber).orElse(null);
		if(driver == null) {
			throw new IllegalStateException(String.format("Driver with id %s doesn't exist",
					report.driverNumber));
		}
		reportRepository.save(new Report(car, report.driverNumber, report.parkingZone,
				report.date, report.cost, report.status, report.driverName));
	}

//	@Override
//	@Transactional
//	public void updateDriver(long driverId, String email) {
//		Driver driver = driverRepository.findById(driverId).orElse(null);
//		if(driver == null) {
//		throw new IllegalStateException(String.format("Driver with id: %s doesn't exist",
//				driverId));
//		}
//		driver.setEmail(email);
//		driverRepository.save(driver);
//	}
//
//	@Override
//	@Transactional
//	public void updateCar(long carNumber, long driverId) {
//		Car car = carRepository.findById(carNumber).orElse(null);
//		if(car == null) {
//			throw new IllegalStateException(String.format("Car with number: %s doesn't exist",
//					carNumber));
//		}
//		Driver driver = driverRepository.findById(driverId).orElse(null);
//		if(driver == null) {
//			throw new IllegalStateException(String.format("Driver with id: %s doesn't exist",
//					driverId));
//		}
//		car.setDriver(driver);
//		carRepository.save(car);
//
//	}
//
//	@Override
//	@Transactional
//	public void updateReport(long reportId, String status) {
//		Report report = reportRepository.findById(reportId).orElse(null);
//		if(report == null) {
//			throw new IllegalStateException(String.format("Report with id: %s doesn't exist",
//					reportId));
//		}
//		report.setStatus(status);
//		reportRepository.save(report);
//
//	}
//
//	@Override
//	public Driver getDriver(long driverId) {
//
//		return driverRepository.findById(driverId).orElse(null);
//	}
//
//	@Override
//	public Car getCar(long carId) {
//
//		return carRepository.findById(carId).orElse(null);
//	}
//
//	@Override
//	public Report getReport(long reportId) {
//
//		return reportRepository.findById(reportId).orElse(null);
//	}
//
//	@Override
//	@Transactional
//	public Report deleteReport(long reportId) {
//		Report report = reportRepository.findById(reportId).orElse(null);
//		if(report == null) {
//			throw new IllegalStateException(String.format("Report with id: %s doesn't exist",
//					reportId));
//		}
//		reportRepository.deleteById(reportId);
//		return report;
//	}
//
//	@Override
//	public List<ReportProjection> getAllReportsByMonthYear(int year, String month) {
//		YearMonth yearMonthObject = YearMonth.of(year, Integer.parseInt(month));
//		int daysInMonth = yearMonthObject.lengthOfMonth();
//		String from = String.format("%s-%s-01T00:00:00", year, month);
//		String to = String.format("%s-%s-%sT23:59:59", year, month, daysInMonth);
//		LOG.debug("days in month: {}, date from: {}, date to: {}", daysInMonth, from, to);
//		List<ReportProjection> reports = reportRepository.getAllReportsByMonthAndYear(from, to);
//		LOG.debug("reports: {}", reports.toString());
//		return reports;
//
//	}






}
