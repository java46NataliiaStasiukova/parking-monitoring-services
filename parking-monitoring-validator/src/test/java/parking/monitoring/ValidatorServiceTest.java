package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

import parking.monitoring.entities.LastCarFine;
import parking.monitoring.repo.LastCarFineRepository;
import parking.monitoring.service.ValidatorService;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class ValidatorServiceTest {
	
	static Logger LOG = LoggerFactory.getLogger(ValidatorServiceTest.class);

	@Autowired
	ValidatorService service;
	@MockBean
	LastCarFineRepository fineRepository;
	
	private static final long FINE_NO_REDIS_DATA = 111l;
	private static final long FINE_NO_ACTION = 222l;
	private static final long FINE_NEW_PARKING_ZONE = 333l;
	private static final long FINE_EXPIRED_DATE = 444l;

	ParkingFine fineNoRedisData = new ParkingFine(FINE_NO_REDIS_DATA, "1");
	ParkingReport reportNoRedisData = new ParkingReport(FINE_NO_REDIS_DATA, "1");
	
	ParkingFine fineNoAction = new ParkingFine(FINE_NO_ACTION, "1");
	LastCarFine lastFineNoAction = new LastCarFine(FINE_NO_ACTION, "1", LocalDate.now());
	
	ParkingFine fineNewZone = new ParkingFine(FINE_NEW_PARKING_ZONE, "2");
	LastCarFine lastFineNewZone = new LastCarFine(FINE_NEW_PARKING_ZONE, "1", LocalDate.now());
	ParkingReport reportNewZone = new ParkingReport(FINE_NEW_PARKING_ZONE, "2");
	
	ParkingFine fineExpiredDate = new ParkingFine(FINE_EXPIRED_DATE, "1");
	LastCarFine lastFineExpiredDate = new LastCarFine(FINE_EXPIRED_DATE, "1", LocalDate.now().minusDays(1));
	ParkingReport reportExpiredDate = new ParkingReport(FINE_EXPIRED_DATE, "1");
	
	@BeforeEach
	void redisMocking() {
		when(fineRepository.findById(FINE_NO_REDIS_DATA)).thenReturn(Optional.ofNullable(null));
		when(fineRepository.findById(FINE_NO_ACTION)).thenReturn(Optional.of(lastFineNoAction));
		when(fineRepository.findById(FINE_NEW_PARKING_ZONE)).thenReturn(Optional.of(lastFineNewZone));
		when(fineRepository.findById(FINE_EXPIRED_DATE)).thenReturn(Optional.of(lastFineExpiredDate));
	}
	
	@Test
	void noRedisDataTest() {
		LOG.debug("**TEST: no redis data");
		assertEquals(reportNoRedisData, service.validate(fineNoRedisData));
	}
	
	@Test 
	void noActionTest(){
		LOG.debug("**TEST: no action");
		assertNull(service.validate(fineNoAction));
	}
	
	@Test 
	void newParkingZoneTest() {
		LOG.debug("**TEST: new parking zone");
		assertEquals(reportNewZone, service.validate(fineNewZone));
	}
	
	@Test
	void expiredTimeTest() {
		LOG.debug("**TEST: time expiered");
		assertEquals(reportExpiredDate, service.validate(fineExpiredDate));
	}

}
