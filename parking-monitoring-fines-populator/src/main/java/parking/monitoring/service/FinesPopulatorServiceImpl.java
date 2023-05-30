package parking.monitoring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import parking.monitoring.dto.ReportDto;
import parking.monitoring.entities.Car;
import parking.monitoring.entities.Driver;
import parking.monitoring.entities.Report;
import parking.monitoring.repo.CarRepository;
import parking.monitoring.repo.DriverRepository;
import parking.monitoring.repo.ReportRepository;


@Service
public class FinesPopulatorServiceImpl implements FinesPopulatorService {
	
	static Logger LOG = LoggerFactory.getLogger(FinesPopulatorServiceImpl.class);
	@Autowired
	ReportRepository reportRepository;
	@Autowired
	DriverRepository driverRepository;
	@Autowired
	CarRepository carRepository;

	@Override
	public void addReport(ReportDto report) {
		Car car = carRepository.findById(report.carNumber).orElse(null);
		if(car == null) {
			LOG.warn("*populator* car with number: {} not exist", report.carNumber);
			throw new IllegalStateException(String.format("Car with number: %s doesn't exist",
					report.carNumber));
		}
		Driver driver = driverRepository.findById(report.driverId).orElse(null);
		if(driver == null) {
			LOG.warn("*populator* driver with id: {} already exist", report.carNumber);
			throw new IllegalStateException(String.format("Driver with id %s doesn't exist",
					report.driverId));
		}
		LOG.debug("*populator* new report: {} was added", report.toString());
		reportRepository.save(new Report(car, report.driverId, report.parkingZone,
				report.date, report.cost, report.status, driver.getName()));	

	}

}
