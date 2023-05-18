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

import parking.monitoring.service.ValidatorService;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class ValidatorControllerTest {

	static Logger LOG = LoggerFactory.getLogger(ValidatorControllerTest.class);
	
	@MockBean
	ValidatorService service;
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	
	private static final long CAR_VALID_FINE = 111;
	private static final long CAR_NOT_VALID_FINE = 222;
	
	ParkingFine validFine = new ParkingFine(CAR_VALID_FINE, "1");
	ParkingFine notValidFine = new ParkingFine(CAR_NOT_VALID_FINE, "2");
	
	ParkingReport report = new ParkingReport(CAR_VALID_FINE, "1");
	
	String bindingNameProducer = "validatorConsumer-in-0";
	@Value("${app.validator.binding.name}")
	String bindingNameConsumer;
	
	@BeforeEach
	void mockingService() {
		when(service.validate(notValidFine)).thenReturn(null);
		when(service.validate(validFine)).thenReturn(report);
	}
	
	@Test
	void notValidFineTest() {
		LOG.debug("***TEST: receiving not valid parking fine");
		producer.send(new GenericMessage<ParkingFine>(notValidFine), bindingNameProducer);
		Message<byte[]> message = consumer.receive(10, bindingNameConsumer);
		assertNull(message);
	}
	
	@Test
	void validFineTest() throws StreamReadException, DatabindException, IOException {
		LOG.debug("***TEST: receiving valid parking fine");
		producer.send(new GenericMessage<ParkingFine>(validFine), bindingNameProducer);
		Message<byte[]> message = consumer.receive(10, bindingNameConsumer);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		ParkingReport parkingReport = mapper.readValue(message.getPayload(), ParkingReport.class);
		assertEquals(report, parkingReport);
	}

}
