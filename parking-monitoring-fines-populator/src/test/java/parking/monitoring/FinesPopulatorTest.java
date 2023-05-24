package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

import parking.monitoring.dto.ReportDto;
import parking.monitoring.entities.Car;
import parking.monitoring.entities.Driver;
import parking.monitoring.entities.Report;
import parking.monitoring.repo.CarRepository;
import parking.monitoring.repo.DriverRepository;
import parking.monitoring.repo.ReportRepository;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
interface TestConstants {
	String FILE_NAME = "test-reports.data";
}

@SpringBootTest(properties = {"TestConstants.FILE_NAME"})
@Import(TestChannelBinderConfiguration.class)
class FinesPopulatorTest {
	
	static Logger LOG = LoggerFactory.getLogger(FinesPopulatorTest.class);
	@Autowired
	InputDestination producer;
//	@Autowired
//	CarRepository carRepository;
//	@Autowired
//	ReportRepository reportRepository;
//	@Autowired
//	DriverRepository driverRepository;
	String bindingName = "reportPopulatorConsumer-in-0";

//	long id;
//	Car car;
//	long driver;
//	String zone;
//	String date;
//	double cost;
//	String status;
//	String name;
	
//	long id;
//	String name;
//	String email;
//	String birthdate;
	ReportDto reportDto = new ReportDto(111, 121, "1", "2023-05-20", 
			250.0, "not-paid", "Yakov");
	Driver driver = new Driver(121, "Yakov", "yakov@gmail.com", "2023-05-20");
	Car car = new Car(111, driver);
	Report report = new Report(car, 121, "1", "2023-05-20", 
			250.0, "not-paid", "Yakov");
	

	@Test
	//@Sql(scripts = {"CarsDriversReports.sql"})
	void test() {
		fail("Not yet implemented");
	}

}
