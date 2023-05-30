package parking.monitoring;

import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import parking.monitoring.dto.ReportDto;
import parking.monitoring.service.FinesPopulatorService;

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
		LOG.debug("*populator* received report dto for car: {}", report.toString());
		service.addReport(report);
	}


}
