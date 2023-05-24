package parking.monitoring;

import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import parking.monitoring.service.FinesAnalyzerService;

@SpringBootApplication
public class FinesAnalyzerAppl {
	static private Logger LOG = LoggerFactory.getLogger(FinesAnalyzerAppl.class);
	@Autowired
	FinesAnalyzerService finesAnalyzer;
	@Autowired
	StreamBridge streamBridge;
	@Value("${app.fines.binding.name:parking-fine-out-0}")
	private String bindingName_1;
	@Value("${app.payment.binding.name:payment-out-0}")
	private String bindingName_2;

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
			LOG.debug("*fines-analyzer* sending new parking fine: {}", parkingFine);
			streamBridge.send(bindingName_1, parkingFine);
		} else {
			LOG.debug("*car-analyzer* recieved parking fine: NULL");
			PaymentData paymentData = finesAnalyzer.checkNewCarScan(car);
			if(paymentData != null) {
				LOG.debug("*fines-analyzer* sending new payment data: {}", paymentData);
				streamBridge.send(bindingName_2, paymentData);
			} else {
				LOG.debug("*car-analyzer* recieved payment data: NULL");
			}
		}
	}

}
