package parking.monitoring;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import parking.monitoring.service.PaymentCheckerService;

@SpringBootApplication
public class PaymentCheckerAppl {
	
	static private Logger LOG = LoggerFactory.getLogger(PaymentCheckerAppl.class);
	@Autowired
	PaymentCheckerService paymentService;
	@Autowired
	StreamBridge streamBridge;
	@Value("${app.fines.binding.name:car-data-out-0}")
	private String bindingName;

	public static void main(String[] args) {
		SpringApplication.run(PaymentCheckerAppl.class, args);

	}
	
	@Bean
	Consumer<PaymentData> paymentCheckerConsumer(){
		return this::checkPayment;
	}
	
	void checkPayment(PaymentData data) {
		NewCarScan car = paymentService.checkPayment(data);
		if(car != null) {
			LOG.debug("*payment-checker* sending new parking fine: {}", car);
			streamBridge.send(bindingName, car);
		} else {
			LOG.debug("*car-analyzer* recieved parking fine: NULL");
		}
	}

}
