package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import parking.monitoring.service.CarAnalyzerService;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class CarAnalyzerControllerTest {
	
	static Logger LOG = LoggerFactory.getLogger(CarAnalyzerControllerTest.class);
	
	@MockBean
	CarAnalyzerService service;
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	
	private static final long CAR = 111l;
	private static final long EXISTING_CAR = 222l;
	
	CarScan newCar = new CarScan(CAR, "1");
	CarScan existingCar = new CarScan(EXISTING_CAR, "1");
	CarScan carWithNewParkingZon = new CarScan(CAR, "2");
	
	NewCarScan newCarScan = new NewCarScan(55, "1");
	NewCarScan CarScanInNewParkingZone = new NewCarScan(55, "2");

	
	String bindingNameProducer = "carScanConsumer-in-0";
	@Value("${app.cars.binding.name}")
	String bindingNameConsumer;

	@BeforeEach
	void mockingService() {
		when(service.processCarScan(newCar)).thenReturn(newCarScan);
		when(service.processCarScan(existingCar)).thenReturn(null);
		when(service.processCarScan(carWithNewParkingZon)).thenReturn(CarScanInNewParkingZone);
	}
	
	@Test
	void receivingNewCarTest() throws StreamReadException, DatabindException, IOException {
		LOG.debug("***TEST: receiving new car scan");
		producer.send(new GenericMessage<CarScan>(newCar), bindingNameProducer);
		Message<byte[]> message = consumer.receive(10, bindingNameConsumer);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		NewCarScan car = mapper.readValue(message.getPayload(), NewCarScan.class);
		assertEquals(newCarScan, car);
	}
	
	@Test
	void receivingExistingCarTest() {
		LOG.debug("***TEST: receiving existing car scan");
		producer.send(new GenericMessage<CarScan>(existingCar), bindingNameProducer);
		Message<byte[]> message = consumer.receive(10, bindingNameConsumer);
		assertNull(message);
	}
	
	@Test
	void receivingCarWithZoneChangedTest() throws StreamReadException, DatabindException, IOException {
		LOG.debug("***TEST: receiving existing car scan with new parking zone");
		producer.send(new GenericMessage<CarScan>(carWithNewParkingZon), bindingNameProducer);
		Message<byte[]> message = consumer.receive(10, bindingNameConsumer);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		NewCarScan car = mapper.readValue(message.getPayload(), NewCarScan.class);
		assertEquals(CarScanInNewParkingZone, car);
	}

}
