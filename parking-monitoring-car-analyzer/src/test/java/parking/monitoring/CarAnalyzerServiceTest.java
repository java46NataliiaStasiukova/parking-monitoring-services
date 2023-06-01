package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

import parking.monitoring.entities.LastScan;
import parking.monitoring.repo.LastScanRepository;
import parking.monitoring.service.CarAnalyzerService;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class CarAnalyzerServiceTest {
	
	static Logger LOG = LoggerFactory.getLogger(CarAnalyzerServiceTest.class);
	
	@Autowired
	CarAnalyzerService service;
	@MockBean
	LastScanRepository lastScanRepository;
	
	private static final long CAR_NO_REDIS_DATA = 111l;
	private static final long CAR_NO_ACTION = 222l;
	private static final long CAR_NEW_PARKING_ZONE = 333l;
	private static final long CAR_EXPIRED_TIME = 444l;
	
	CarScan carNoRedis = new CarScan(CAR_NO_REDIS_DATA, "1");
	NewCarScan newCarScanNoRedis = new NewCarScan(CAR_NO_REDIS_DATA, "1");
	
	CarScan carNoAction = new CarScan(CAR_NO_ACTION, "1");
	LastScan lastScanNoAction = new LastScan(CAR_NO_ACTION, "1", LocalDateTime.now().plusMinutes(10));
	
	CarScan carNewZone = new CarScan(CAR_NEW_PARKING_ZONE, "2");
	LastScan lastScanNewZone = new LastScan(CAR_NEW_PARKING_ZONE, "1", LocalDateTime.now());
	NewCarScan newCarScanNewZone = new NewCarScan(CAR_NEW_PARKING_ZONE, "2");
	
	CarScan carExpiredTime = new CarScan(CAR_EXPIRED_TIME, "1");
	LastScan lastScanExpiredTime = new LastScan(CAR_EXPIRED_TIME, "1", LocalDateTime.now().minusMinutes(10));
	NewCarScan newCarScanExpiredTime = new NewCarScan(CAR_EXPIRED_TIME, "1");

	@BeforeEach
	void redisMocking() {
		when(lastScanRepository.findById(CAR_NO_REDIS_DATA)).thenReturn(Optional.ofNullable(null));
		when(lastScanRepository.findById(CAR_NO_ACTION)).thenReturn(Optional.ofNullable(lastScanNoAction));
		when(lastScanRepository.findById(CAR_NEW_PARKING_ZONE)).thenReturn(Optional.of(lastScanNewZone));
		when(lastScanRepository.findById(CAR_EXPIRED_TIME)).thenReturn(Optional.of(lastScanExpiredTime));
	}
	
	@Test
	void noRedisDataTest() {
		LOG.debug("**TEST: no redis data");
		assertEquals(newCarScanNoRedis, service.processCarScan(carNoRedis));
	}
	
	@Test 
	void noActionTest(){
		LOG.debug("**TEST: no action");
		assertNull(service.processCarScan(carNoAction));
	}
	
	@Test 
	void newParkingZoneTest() {
		LOG.debug("**TEST: new parking zone");
		assertEquals(newCarScanNewZone, service.processCarScan(carNewZone));
	}
	
	@Test
	void expiredTimeTest() {
		LOG.debug("**TEST: time expiered");
		assertEquals(newCarScanExpiredTime, service.processCarScan(carExpiredTime));
	}

}
