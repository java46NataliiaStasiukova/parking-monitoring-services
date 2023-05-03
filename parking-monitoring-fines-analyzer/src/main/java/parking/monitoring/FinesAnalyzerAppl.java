package parking.monitoring;

import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import parking.monitoring.service.FinesAnalyzerService;
import parking.monitoring.service.FinesAnalyzerServiceImpl;

@SpringBootApplication
public class FinesAnalyzerAppl {
	static private Logger LOG = LoggerFactory.getLogger(FinesAnalyzerAppl.class);
	@Autowired
	FinesAnalyzerService finesAnalyzer;
	@Autowired
	StreamBridge streamBridge;
	@Value("${app.fines.binding.name:parking-fine-out-0}")
	private String bindingName;

	public static void main(String[] args) {
		SpringApplication.run(FinesAnalyzerAppl.class, args);

	}
	
	@Bean
	Consumer<NewCarScan> parkingPaymentConsumer(){
		
		return this::finesAnalyzer;
	}
	
	void finesAnalyzer(NewCarScan car) {
		ParkingFine parkingFine = finesAnalyzer.processNewCarScan(car);
		if(parkingFine != null) {
			streamBridge.send(bindingName, parkingFine);
		}
	}

}
