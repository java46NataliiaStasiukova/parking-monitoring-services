package parking.monitoring.controller;

import java.util.List;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import parking.monitoring.dto.CarDto;
import parking.monitoring.dto.DriverDto;
import parking.monitoring.dto.ReportDto;
import parking.monitoring.entities.Car;
import parking.monitoring.entities.Driver;
import parking.monitoring.entities.Report;
import parking.monitoring.proj.ReportProjection;
import parking.monitoring.service.BackOfficeService;

@RestController
@RequestMapping("parking")
@Validated
public class BackOfficeController {

	private static final String STATUS_REGEX = "paid|not-paid|canceled";
	private static final String STATUS_MESSAGE = "should be either: paid, not-paid or canceled";
	static Logger LOG = LoggerFactory.getLogger(BackOfficeController.class);
	@Autowired
	BackOfficeService service;
	
	@PostMapping("/driver")
	DriverDto addDriver(@RequestBody @Valid DriverDto driver) {
		LOG.debug("*back-office* add: received driver dto: {}", driver.toString());
		service.addDriver(driver);
		return driver;
	}

	@PostMapping("/car")
	CarDto addCar(@RequestBody @Valid CarDto car) {
		LOG.debug("*back-office* add: received car dto: {}", car.toString());
		service.addCar(car);
		return car;
	}

	@PostMapping("/report")
	ReportDto addReport(@RequestBody @Valid ReportDto report) {
		LOG.debug("a*back-office* dd: received report dto: {}", report.toString());
		service.addReport(report);
		return report;
	}

	@PutMapping("/driver")
	String updateDriver(@RequestParam("driverId") @NotNull Long driverId,
			@RequestParam("email") @Email String email) {
		LOG.debug("*back-office* request for update: received driver id: {}", driverId);
		service.updateDriver(driverId, email);
		return String.format("driver with id %s was updated", driverId);
	}

	@PutMapping("/car")
	String updateCar(@RequestParam("carNumber") @NotNull Long carNumber,
			@RequestParam @NotNull Long driverId) {
		LOG.debug("*back-office* request for update: received car number: {}", carNumber);
		service.updateCar(carNumber, driverId);
		return String.format("car with car number %s was updated", carNumber);
	}

	@PutMapping("/report")
	String updateReport(@RequestParam("reportId") @NotNull Long reportId,
			@RequestParam("status") @Pattern(regexp = STATUS_REGEX, message = STATUS_MESSAGE) String status) {
		LOG.debug("*back-office* request for update: received report id: {}", reportId);
		service.updateReport(reportId, status);
		return String.format("report with id %s was updated", reportId);
	}

	//TOFIX
	@GetMapping("/driver/{driverId}")
	String getDriver(@PathVariable long driverId) {
		LOG.debug("*back-office* request for getting driver by id: {}", driverId);
		String res = String.format("driver with id %s doesn't exist", driverId);
		Driver driver = service.getDriver(driverId);
		if(driver != null) {
			res = String.format("found driver: driver id: %s, name: %s, email: %s,"
					+ "birthdate: %s", driver.getId(), driver.getName(),
					driver.getEmail(), driver.getBirthdate());
		}
		return res;
	}

	//TOFIX
	@GetMapping("/car/{carNumber}")
	String getCar(@PathVariable long carNumber) {
		LOG.debug("*back-office* request for getting car by car number: {}", carNumber);
		String res = String.format("car with number %s doesn't exist", carNumber);
		Car car = service.getCar(carNumber);
		if(car != null) {
			res = String.format("found car: car number: %s, driver id: %s",
					car.getNumber(), car.getDriver().getId());
		}
		return res;
	}

	//TOFIX
	@GetMapping("/report/{reportId}")
	String getReport(@PathVariable long reportId) {
		LOG.debug("*back-office* request for getting report by id: {}", reportId);
		String res = String.format("report with id %s doesn't exist", reportId);
		Report report = service.getReport(reportId);
		if(report != null) {
			res = String.format("found report: report id: %s, car number: %s, "
					+ "driver id: %s, parking zone: %s, date: %s, cost: %s, "
					+ "status: %s, driver name: %s", reportId, report.getCar().getNumber(),
					report.getId(), report.getZone(), report.getDate(),
					report.getCost(), report.getStatus(), report.getName());
		}
		return res;
	}
	
	@GetMapping("/driver/getByCar/{carNumber}")
	String getDriverByCarNumber(@PathVariable long carNumber) {
		LOG.debug("*back-office* request for getting driver by car number: {}", carNumber);
		String res = String.format("driver not exist for car with number: %s", carNumber);
		Driver driver = service.getDriverByCarNumber(carNumber);
		if(driver != null) {
			res = String.format("found for car with number: %s, driver: driver id: %s, name: %s, email: %s,"
					+ "birthdate: %s", carNumber, driver.getId(), driver.getName(),
					driver.getEmail(), driver.getBirthdate());
		}
		return res;
	}

	@DeleteMapping("/report/{reportId}")
	String deleteReport(@PathVariable long reportId) {
		LOG.debug("*back-office* request to delete report with id: {}", reportId);
		String res = String.format("report with id: %s doesn't exist", reportId);
		Report report = service.deleteReport(reportId);
		if(report != null) {
			res = String.format("report with id: %s was removed", reportId);
		}
		return res;
	}
	
	@DeleteMapping("/car/{carNumber}")
	String deleteCar(@PathVariable long carNumber) {
		LOG.debug("*back-office* request to delete car with number: {}", carNumber);
		String res = String.format("car with number: %s doesn't exist", carNumber);
		Car car = service.deleteCar(carNumber);
		if(car != null) {
			res = String.format("car with number: %s was removed", carNumber);
		}
		return res;
	}
	
	@DeleteMapping("/driver/{driverId}")
	String deleteDriver(@PathVariable long driverId) {
		LOG.debug("*back-office* request to delete driver with id: {}", driverId);
		String res = String.format("driver with id: %s doesn't exist", driverId);
		Driver driver = service.deleteDriver(driverId);
		if(driver != null) {
			res = String.format("driver with id: %s was removed", driverId);
		}
		return res;
	}

	@GetMapping("/report/getByMonth/{year}/{month}")
	List<ReportProjection> getReportsByMonth(@PathVariable int year,
			@PathVariable String month) {
		LOG.debug("*back-office* request for getting reports by year: {} and month: {}", year, month);
		return service.getAllReportsByMonthYear(year, month);
	}
	
	@GetMapping("/report/getByAge/{age}")
	List<ReportProjection> getByAge(@PathVariable int age){
		LOG.debug("*back-office* request for getting reports by driver age: {}", age);
		return service.getAllReportsByDriverAge(age);
	}
	
	@GetMapping("/report/getByDriver/{driverId}")
	List<ReportProjection> getReportsByDriverId(@PathVariable long driverId){
		LOG.debug("*back-office* request for getting reports by driver id: {}", driverId);
		return service.getAllReportsByDriverId(driverId);
	}
	
	@GetMapping("/report/getByCar/{carNumber}")
	List<ReportProjection> getReportsByCarNumber(@PathVariable long carNumber){
		LOG.debug("*back-office* request for getting reports by car number: {}", carNumber);
		return service.getAllReportsByCarNumber(carNumber);
	}
	
	@GetMapping("/report/getCanceled")
	List<ReportProjection> getCanceledReports(){
		LOG.debug("*back-office* request for getting all canceled reports");
		return service.getAllCanceledReports();
	}
	
	@GetMapping("/report/getCanceled/{carNumber}")
	List<ReportProjection> getCanceledReportsByCarNumber(@PathVariable long carNumber){
		LOG.debug("*back-office* request for getting canceled reports by car number: {}", carNumber);
		return service.getAllCanceledReportsByCarNumber(carNumber);
	}
	
	@GetMapping("/report/getNotPaid/{carNumber}")
	List<ReportProjection> getNotPaidReportsByCarNumber(@PathVariable long carNumber){
		LOG.debug("*back-office* request for getting not paid reports by car number: {}", carNumber);
		return service.getAllNotPaidReportsByCarNumber(carNumber);
	}


}
