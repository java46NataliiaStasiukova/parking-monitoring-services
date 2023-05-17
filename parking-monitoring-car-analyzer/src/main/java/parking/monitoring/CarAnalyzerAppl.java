package parking.monitoring;

import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import parking.monitoring.service.CarAnalyzerService;

@SpringBootApplication
public class CarAnalyzerAppl {
	
	@Autowired
	CarAnalyzerService carAnalyzerService;
	@Autowired
	StreamBridge streamBridge;
	@Value("${app.cars.binding.name:car-data-out-0}")
	private String bindingName;
	static Logger LOG = LoggerFactory.getLogger(CarAnalyzerAppl.class);
	
	public static void main(String[] args) {
		SpringApplication.run(CarAnalyzerAppl.class, args);

	}
	
	@Bean
	Consumer<CarScan> carScanConsumer(){
		return this::carScanAnalyzer;
	}
	
	void carScanAnalyzer(CarScan car) {
		NewCarScan newCar = carAnalyzerService.processCarScan(car);
		if(newCar != null) {
			LOG.debug("*car-analyzer* sending new car scan: {}", newCar);
			streamBridge.send(bindingName, newCar);
		} else {
			LOG.debug("*car-analyzer* recieved car scan: NULL");
		}
	}
	

}
