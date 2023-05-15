package parking.monitoring.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import parking.monitoring.dto.*;
import parking.monitoring.entities.*;
import parking.monitoring.proj.*;
import parking.monitoring.repo.*;

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
			LOG.warn("*back-office* driver with id: {} already exist", driver.id);
			throw new IllegalStateException(String.format("Driver with id: %s already exist",
					driver.id));
		}
		LOG.debug("*back-office* was added new driver: {}", driver.toString());
		driverRepository.save(new Driver(driver.id, driver.name, driver.email, driver.birthdate));
	}

	@Override
	@Transactional
	public void addCar(CarDto car) {
		if(carRepository.existsById(car.carNumber)) {
			LOG.warn("*back-office* car with number: {} already exist", car.carNumber);
			throw new IllegalStateException(String.format("Car with number: %s already exist",
					car.carNumber));
		}
		Driver driver = driverRepository.findById(car.driverId).orElse(null);
		if(driver == null) {
			LOG.warn("*back-office* driver with id: {} not exist", car.driverId);
			throw new IllegalStateException(String.format("Driver with id: %s doesn't exist",
					car.driverId));
		}
		LOG.debug("*back-office* was added new car: {}", car.toString());
		carRepository.save(new Car(car.carNumber, driver));
	}

	@Override
	@Transactional
	public void addReport(ReportDto report) {
		Car car = carRepository.findById(report.carNumber).orElse(null);
		if(car == null) {
			LOG.warn("*back-office* car with number: {} not exist", report.carNumber);
			throw new IllegalStateException(String.format("Car with number: %s doesn't exist",
					report.carNumber));
		}
		Driver driver = driverRepository.findById(report.driverNumber).orElse(null);
		if(driver == null) {
			LOG.warn("*back-office* driver with id: {} already exist", report.carNumber);
			throw new IllegalStateException(String.format("Driver with id %s doesn't exist",
					report.driverNumber));
		}
		LOG.debug("*back-office* new report: {} was added", report.toString());
		reportRepository.save(new Report(car, report.driverNumber, report.parkingZone,
				report.date, report.cost, report.status, driver.getName()));
	}

	@Override
	@Transactional
	public void updateDriver(long driverId, String email) {
		Driver driver = driverRepository.findById(driverId).orElse(null);
		if(driver == null) {
			LOG.warn("*back-office* driver with id: {} not exist", driverId);
		throw new IllegalStateException(String.format("Driver with id: %s doesn't exist",
				driverId));
		}
		LOG.debug("*back-office* driver with id: {} was updated", driverId);
		driver.setEmail(email);
		//driverRepository.save(driver);
	}

	@Override
	@Transactional
	public void updateCar(long carNumber, long driverId) {
		Car car = carRepository.findById(carNumber).orElse(null);
		if(car == null) {
			LOG.warn("*back-office* car with number: {} not exist", carNumber);
			throw new IllegalStateException(String.format("Car with number: %s doesn't exist",
					carNumber));
		}
		Driver driver = driverRepository.findById(driverId).orElse(null);
		if(driver == null) {
			LOG.warn("*back-office* driver with id: {} not exist", driverId);
			throw new IllegalStateException(String.format("Driver with id: %s doesn't exist",
					driverId));
		}
		LOG.debug("*back-office* car with number: {} was updated", carNumber);
		car.setDriver(driver);
		//carRepository.save(car);

	}

	@Override
	@Transactional
	public void updateReport(long reportId, String status) {
		Report report = reportRepository.findById(reportId).orElse(null);
		if(report == null) {
			LOG.warn("*back-office* report with id: {} not exist", reportId);
			throw new IllegalStateException(String.format("Report with id: %s doesn't exist",
					reportId));
		}
		LOG.debug("*back-office* report with id: {} was updated", reportId);
		report.setStatus(status);
		//reportRepository.save(report);

	}

	@Override
	public Driver getDriver(long driverId) {

		return driverRepository.findById(driverId).orElse(null);
	}

	@Override
	public Car getCar(long carId) {

		return carRepository.findById(carId).orElse(null);
	}

	@Override
	public Report getReport(long reportId) {

		return reportRepository.findById(reportId).orElse(null);
	}

	@Override
	@Transactional
	public Report deleteReport(long reportId) {
		Report report = reportRepository.findById(reportId).orElse(null);
		if(report == null) {
			LOG.warn("*back-office* report with id: {} not exist", reportId);
			throw new IllegalStateException(String.format("Report with id: %s doesn't exist",
					reportId));
		}
		LOG.debug("*back-office* report with id: {} was deleted", reportId);
		reportRepository.deleteById(reportId);
		return report;
	}
	
	@Override
	@Transactional
	public Car deleteCar(long carNumber) {
		Car car = carRepository.findById(carNumber).orElse(null);
		if(car == null) {
			LOG.warn("*back-office* car with number: {} not exist", carNumber);
			throw new IllegalStateException(String.format("Car with number: %s doesn't exist",
					carNumber));
		}
		List<Report> reports = reportRepository.findReportByCarNumber(carNumber);
		if(reports.size() > 0) {
			reports.forEach(reportRepository::delete);
			LOG.warn("*back-office* reports for car with number: {} was deleted", carNumber);
		}
		carRepository.deleteById(carNumber);
		LOG.debug("*back-office* car with number: {} was deleted", carNumber);
		return car;
	}

	@Override
	@Transactional
	public Driver deleteDriver(long driverId) {
		Driver driver = driverRepository.findById(driverId).orElse(null);
		if(driver == null) {
			LOG.warn("*back-office* driver with id: {} not exist", driverId);
			throw new IllegalStateException(String.format("Driver with id: %s doesn't exist",
					driverId));
		}
		Car car = carRepository.findCarByDriverId(driverId);
		if(car != null) {
			List<Report> reports = reportRepository.findReportByCarNumber(car.getCarNumber());
			if(reports.size() > 0) {
				reports.forEach(reportRepository::delete);
				LOG.warn("*back-office* reports for car with number: {} was deleted", car.getCarNumber());
			}
			carRepository.deleteById(car.getCarNumber());
			LOG.warn("*back-office* car with number: {} was deleted for driver with id: {}", car.getCarNumber(), driverId);
		}
		driverRepository.deleteById(driverId);
		LOG.debug("*back-office* driver with id: {} was deleted", driverId);
		return driver;
	}

	@Override
	public List<ReportProjection> getAllReportsByMonthYear(int year, String month) {
		YearMonth yearMonthObject = YearMonth.of(year, Integer.parseInt(month));
		int daysInMonth = yearMonthObject.lengthOfMonth();
		String from = String.format("%s-%s-01T00:00:00", year, month);
		String to = String.format("%s-%s-%sT23:59:59", year, month, daysInMonth);
		LOG.debug("days in month: {}, date from: {}, date to: {}", daysInMonth, from, to);
		List<ReportProjection> reports = reportRepository.getAllReportsByMonthAndYear(from, to);
		LOG.debug("reports: {}", reports.toString());
		return reports;

	}

//	@Override
//	public List<ReportProjection> getAllReportsByDriverAge(int age) {
//		int year = LocalDate.now().getYear() - age;
//		return reportRepository.getReportsByAge(year - 1, year + 1);
//	}






}
