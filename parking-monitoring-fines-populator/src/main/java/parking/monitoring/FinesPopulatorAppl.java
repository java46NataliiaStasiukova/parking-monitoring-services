package parking.monitoring;

import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import parking.monitoring.dto.ReportDto;
import parking.monitoring.entities.Car;
import parking.monitoring.entities.Driver;
import parking.monitoring.entities.Report;
import parking.monitoring.repo.CarRepository;
import parking.monitoring.repo.DriverRepository;
import parking.monitoring.repo.ReportRepository;

@SpringBootApplication
public class FinesPopulatorAppl {
	
	static Logger LOG = LoggerFactory.getLogger(FinesPopulatorAppl.class);
	@Autowired
	ReportRepository reportRepository;
	@Autowired
	DriverRepository driverRepository;
	@Autowired
	CarRepository carRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(FinesPopulatorAppl.class, args);

	}
	
	@Bean
	Consumer<ReportDto> reportPopulatorConsumer(){
		return this::getReport;
	}
	
	void getReport(ReportDto report) {
		LOG.debug("*populator* received report of car with number: {}", report.carNumber);
		addReport(report);
	}

	private void addReport(ReportDto report) {
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
//		reportRepository.save(new Report(car, report.driverNumber, report.parkingZone,
//				report.date, report.cost, report.status, driver.getName()));	
	}

}
