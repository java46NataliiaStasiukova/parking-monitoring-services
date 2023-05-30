package parking.monitoring;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import parking.monitoring.dto.CarDto;
import parking.monitoring.dto.DriverDto;
import parking.monitoring.dto.ReportDto;
import parking.monitoring.service.BackOfficeService;

@Component
public class RandomDBCreation {

	static Logger LOG = LoggerFactory.getLogger(RandomDBCreation.class);
	@Value("${spring.jpa.hibernate.ddl-auto: create}")
	String ddlAutoProp;
	@Autowired
	BackOfficeService service;
	int count = 1;

	@Value("${app.cars.amount:5}")
	private int nReports;
	@Value("${app.min.fines.cost:250}")
	private int minCost;
	@Value("${app.max.fines.cost:950}")
	private int maxCost;
	@Value("${app.parking.zone:1, 2, 3}")
	private String[] parkingZone;
	@Value("${app.min.driver.age:19}")
	private int minAge;
	@Value("${app.max.driver.age:65}")
	private int maxAge;
	@Value("${app.drivers.amount:100}")
	private int nDrivers;

	String driverNames[] = {"Abraham", "Sarah",
			"Itshak", "Rahel", "Asaf", "Yacob", "Rivka",
			"Yosef", "Benyanim", "Dan", "Ruben", "Moshe",
			"Aron", "Yehashua", "David", "Salomon",
			"Nefertity", "Naftaly", "Natan", "Asher"};

	@PostConstruct
	void createDB() {
		if(ddlAutoProp.equals("create")) {
			addDriver();
			addCar();
			addReport();
//			addParkingZone();
			LOG.info("*random db* new db was created with drivers: {}, reports amount: {}", nDrivers, nReports);
		} else {
			LOG.warn("*random db* new db was not created");
		}
	}

	private void addDriver() {
		IntStream.range(0, nDrivers)
		.forEach(i -> {
			service.addDriver(new DriverDto((long)(i + 1), driverNames[getRandomInt(0, driverNames.length - 1)],
					"driver" + (i + 1) + "@gmail.com",
					LocalDate.now().minusYears(getRandomInt(minAge, maxAge)).toString()));
		});

	}

	private void addCar() {
		IntStream.range(1, nDrivers + 1)
		.forEach(i -> {
			service.addCar(new CarDto((long)i, (long)i));
		});
	}

	private void addReport() {
		IntStream.range(0, nReports)
		.forEach(i -> {
			service.addReport(new ReportDto((long)(i + 1), (long)(i + 1), parkingZone[getRandomInt(0, parkingZone.length - 1)],
					LocalDateTime.now().minusDays(1).toString(),
					(double)getRandomInt(minCost, maxCost), "paid", driverNames[i]));
		});
	}

//	private void addParkingZone() {
//		IntStream.range(0, parkingZone.length)
//		.forEach(i -> {
//			service.addParkingZone(new ParkingZoneDto(parkingZone[i],
//					(double)getRandomInt(minCost, maxCost)));
//		});
//	}

	private int getRandomInt(int min, int max) {

		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

}
