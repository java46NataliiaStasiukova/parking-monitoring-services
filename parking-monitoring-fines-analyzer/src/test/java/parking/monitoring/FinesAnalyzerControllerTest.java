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

import parking.monitoring.service.FinesAnalyzerService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class FinesAnalyzerControllerTest {
	
	static Logger LOG = LoggerFactory.getLogger(FinesAnalyzerControllerTest.class);
	
	@MockBean
	FinesAnalyzerService service;
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	String bindingNameProducer = "parkingPaymentConsumer-in-0";
	@Value("${app.fines.binding.name}")
	String bindingNameConsumer;
	private static final long NOT_FINED = 111;
	private static final long FINED = 222;
	
	NewCarScan carWithPayment = new NewCarScan(NOT_FINED, "1");
	NewCarScan carWithNoPayment = new NewCarScan(FINED, "1");
	
	ParkingFine parkingFine = new ParkingFine(FINED, "1");
	
	@BeforeEach
	void mockingService() {
		when(service.processNewCarScan(carWithPayment)).thenReturn(null);
		when(service.processNewCarScan(carWithNoPayment)).thenReturn(parkingFine);
	}

	@Test
	void recivingCarWithPayment() {
		LOG.debug("***TEST: receiving car with status paid");
		producer.send(new GenericMessage<NewCarScan>(carWithPayment), bindingNameProducer);
		Message<byte[]> message = consumer.receive(10, bindingNameConsumer);
		assertNull(message);
	}
	
	@Test
	void recivingCarWithNoPayment() throws StreamReadException, DatabindException, IOException {
		LOG.debug("***TEST: receiving car with status not-paid");
		producer.send(new GenericMessage<NewCarScan>(carWithNoPayment), bindingNameProducer);
		Message<byte[]> message = consumer.receive(10, bindingNameConsumer);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		ParkingFine fine = mapper.readValue(message.getPayload(), ParkingFine.class);
		assertEquals(parkingFine, fine);
	}

}
