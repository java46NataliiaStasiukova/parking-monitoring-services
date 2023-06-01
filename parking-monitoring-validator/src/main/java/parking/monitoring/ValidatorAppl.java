package parking.monitoring;

import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import jakarta.annotation.PreDestroy;
import parking.monitoring.service.ValidatorService;

@SpringBootApplication
public class ValidatorAppl {
	static private Logger LOG = LoggerFactory.getLogger(ValidatorAppl.class);
	@Autowired
	ValidatorService validatorService;
	@Autowired
	StreamBridge streamBridge;
	@Value("${app.validator.binding.name:report-out-0}")
	private String bindingName;
	
	
	public static void main(String[] args) {
		SpringApplication.run(ValidatorAppl.class, args);

	}
	
	@Bean
	Consumer<ParkingFine> validatorConsumer(){
		
		return this::validator;
	}
	
	void validator(ParkingFine parkingFine) {
		ParkingReport report = validatorService.validate(parkingFine);
		if(report != null) {
			LOG.debug("*validator* sending new parking report for car: {}", report.toString());
			streamBridge.send(bindingName, report);
		} else {
			LOG.debug("*validator* recieved parking report: NULL");
		}
	}
	
	@PreDestroy
	void preDestroy() {
		System.out.println("ValidatorAppl - shutdown has been performed");
	}

}
