package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import parking.monitoring.service.PaymentCheckerService;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class PaymentCheckerTest {
	
	static Logger LOG = LoggerFactory.getLogger(PaymentCheckerTest.class);

	@MockBean
	PaymentCheckerService service;
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	String bindingNameProducer = "paymentCheckerConsumer-in-0";
	@Value("${app.fines.binding.name}")
	String bindingNameConsumer;
	
	private static final long CAR_NO_FINE = 111;
	private static final long CAR_WITH_FINE = 222;
	
	PaymentData dataNoFine = new PaymentData(CAR_NO_FINE, "1");
	PaymentData dataWithFine = new PaymentData(CAR_WITH_FINE, "1");
	NewCarScan carScan = new NewCarScan(CAR_WITH_FINE, "1");
	
	@BeforeEach
	void MockingService() {
		when(service.checkPayment(dataNoFine)).thenReturn(null);
		when(service.checkPayment(dataWithFine)).thenReturn(carScan);
	}
	
	@Test
	void recivingCarNoFineTest() {
		LOG.debug("***TEST: receiving payment data with no fine");
		producer.send(new GenericMessage<PaymentData>(dataNoFine), bindingNameProducer);
		Message<byte[]> message = consumer.receive(10, bindingNameConsumer);
		assertNull(message);
	}
	
	void recivingCarWithFineTest() throws StreamReadException, DatabindException, IOException {
		LOG.debug("***TEST: receiving payment data with no fine");
		producer.send(new GenericMessage<PaymentData>(dataWithFine), bindingNameProducer);
		Message<byte[]> message = consumer.receive(10, bindingNameConsumer);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		ParkingFine fine = mapper.readValue(message.getPayload(), ParkingFine.class);
		assertEquals(carScan, fine);
	}

}
