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
import parking.monitoring.service.FinesPopulatorService;
import parking.monitoring.service.FinesPopulatorServiceImpl;

@SpringBootApplication
public class FinesPopulatorAppl {
	
	static Logger LOG = LoggerFactory.getLogger(FinesPopulatorAppl.class);
	@Autowired
	FinesPopulatorService service;
	
	public static void main(String[] args) {
		SpringApplication.run(FinesPopulatorAppl.class, args);

	}
	
	@Bean
	Consumer<ReportDto> reportPopulatorConsumer(){
		
		return this::getReport;
	}
	
	void getReport(ReportDto report) {
		LOG.debug("*populator* received report of car with number: {}", report.carNumber);
		service.addReport(report);
	}


}
