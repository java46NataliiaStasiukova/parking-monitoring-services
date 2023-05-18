package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

import parking.monitoring.entities.LastCarPayment;
import parking.monitoring.repo.LastCarPaymentRepository;
import parking.monitoring.service.FinesAnalyzerService;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class FinesAnalyzerServiceTest {
	
	static Logger LOG = LoggerFactory.getLogger(FinesAnalyzerServiceTest.class);
	
	@Autowired
	FinesAnalyzerService service;

	@MockBean
	LastCarPaymentRepository paymentRepository;
	
	private static final long CAR_NO_REDIS_DATA = 111;
	private static final long CAR_NO_ACTION = 222;
	private static final long CAR_STATUS_NOT_PAID = 333;
	private static final long CAR_EXPIRED_TIME = 444;
	private static final long CAR_NEW_PARKING_ZONE = 555;
	
	NewCarScan carNoRedisData = new NewCarScan(CAR_NO_REDIS_DATA, "1");
	ParkingFine fineNoRedis = new ParkingFine(CAR_NO_REDIS_DATA, "1");
	
	NewCarScan carNoAction = new NewCarScan(CAR_NO_ACTION, "1");
	LastCarPayment carPaymentNoAction = new LastCarPayment(CAR_NO_ACTION, "1", "paid", LocalDateTime.now().plusHours(1));
	
	NewCarScan carStatusNotPaid = new NewCarScan(CAR_STATUS_NOT_PAID, "1");
	LastCarPayment carPaymentNotPaid = new LastCarPayment(CAR_NO_ACTION, "1", "not-paid", LocalDateTime.now().plusHours(1));
	ParkingFine fineNoPayment = new ParkingFine(CAR_STATUS_NOT_PAID, "1");
	
	NewCarScan carExpiredTime = new NewCarScan(CAR_EXPIRED_TIME, "1");
	LastCarPayment carPaymentExpired = new LastCarPayment(CAR_EXPIRED_TIME, "1", "paid", LocalDateTime.now().minusHours(1));
	ParkingFine fineTimeExpired = new ParkingFine(CAR_EXPIRED_TIME, "1");
	
	NewCarScan carNewParkingZone = new NewCarScan(CAR_NEW_PARKING_ZONE, "2");
	LastCarPayment lastPaymentNewZone = new LastCarPayment(CAR_NEW_PARKING_ZONE, "1", "paid", LocalDateTime.now().plusHours(1));
	ParkingFine fineNewParkingZone = new ParkingFine(CAR_NEW_PARKING_ZONE, "2");
	
	@BeforeEach
	void redisMocking() throws Exception{
		when(paymentRepository.findById(CAR_NO_REDIS_DATA)).thenReturn(Optional.ofNullable(null));
		when(paymentRepository.findById(CAR_NO_ACTION)).thenReturn(Optional.of(carPaymentNoAction));
		when(paymentRepository.findById(CAR_STATUS_NOT_PAID)).thenReturn(Optional.of(carPaymentNotPaid));
		when(paymentRepository.findById(CAR_EXPIRED_TIME)).thenReturn(Optional.of(carPaymentExpired));
		when(paymentRepository.findById(CAR_NEW_PARKING_ZONE)).thenReturn(Optional.of(lastPaymentNewZone));
	}
	
	@Test
	void noRedisDataTest() {
		LOG.debug("***TEST: no redis data test");
		assertEquals(fineNoRedis, service.processNewCarScan(carNoRedisData));
	}
	
	@Test
	void noActionTest() {
		LOG.debug("***TEST: no action test");
		assertNull(service.processNewCarScan(carNoAction));
	}
	
	@Test
	void noPaymentTest() {
		LOG.debug("***TEST: car with status not paid");
		assertEquals(fineNoPayment, service.processNewCarScan(carStatusNotPaid));
	}
	
	@Test
	void timeExpiredTest() {
		LOG.debug("***TEST: time expired for car");
		assertEquals(fineTimeExpired, service.processNewCarScan(carExpiredTime));
	}
	
	@Test
	void newParkingZoneTest() {
		LOG.debug("***TEST: parking zone was changed for car");
		assertEquals(fineNewParkingZone, service.processNewCarScan(carNewParkingZone));
	}
	
    

}
