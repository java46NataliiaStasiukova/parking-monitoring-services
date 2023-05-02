package parking.monitoring;

import java.util.function.Consumer;

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
	@Value("${app.cars.binding.name: car_data-out-0}")
	private String bindingName;
	
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
			streamBridge.send(bindingName, newCar);
		}
	}
	

}
