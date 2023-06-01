package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import parking.monitoring.dto.ParkingZoneDto;
import parking.monitoring.entities.ParkingZone;
import parking.monitoring.repo.ParkingZoneRepo;
import parking.monitoring.service.ParkingZonesService;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ZonesBackOfficeServiceTest {
	
	static Logger LOG = LoggerFactory.getLogger(ZonesBackOfficeServiceTest.class);
	@Autowired
	ParkingZoneRepo parkingRepo;
	@Autowired
	ParkingZonesService service;
	
	ParkingZoneDto parkingDto1 = new ParkingZoneDto("1", 200.0, "Bat Yam", "Yerushalaim");
	ParkingZone parkingZone1 = new ParkingZone("1", 200.0, "Bat Yam", "Yerushalaim");
	ParkingZoneDto parkingDto2 = new ParkingZoneDto("1", 250.0, "Bat Yam", "Yerushalaim");
	ParkingZone parkingZone2 = new ParkingZone("1", 250.0, "Bat Yam", "Yerushalaim");
	ParkingZoneDto parkingDto3 = new ParkingZoneDto("3", 200.0, "Bat Yam", "Yerushalaim");

	@Test
	@Order(1)
	void addZoneTest() {
		LOG.debug("***TEST-1 add new parking zone");
		ParkingZone res = service.addParkingZone(parkingDto1);
		assertEquals(res.toString(), parkingZone1.toString());
	}
	
	@Test
	@Order(2)
	void getZoneTest1() {
		LOG.debug("***TEST-2 get parking zone");
		ParkingZone res = service.getParkingZone("1");
		assertEquals(res.toString(), parkingZone1.toString());
	}
	
	@Test
	@Order(3)
	void updateZoneTest() {
		LOG.debug("***TEST-3 update parking zone");
		ParkingZone res = service.updateParkingZone(parkingDto2);
		assertEquals(res.toString(), parkingZone2.toString());
	}
	
	@Test
	@Order(4)
	void getZoneTest2() {
		LOG.debug("***TEST-4 get updated parking zone");
		ParkingZone res = service.getParkingZone("1");
		assertEquals(res.toString(), parkingZone2.toString());
	}
	
	@Test
	@Order(5)
	void deleteZoneTest() {
		LOG.debug("***TEST-5 delete parking zone");
		ParkingZone res = service.deleteParkingZone("1");
		assertEquals(res.toString(), parkingZone2.toString());
	}
	
	@Test
	@Order(6)
	void deleteNoExistZoneTest() {
		LOG.debug("***TEST-5 delete no exist parking zone");
		ParkingZone res = service.deleteParkingZone("3");
		assertNull(res);
	}
	
	@Test
	@Order(7)
	void getNoExistZoneTest() {
		LOG.debug("***TEST-5 get no exist parking zone");
		ParkingZone res = service.getParkingZone("3");
		assertNull(res);
	}
	
	@Test
	@Order(8)
	void updateNoExistZoneTest() {
		LOG.debug("***TEST-5 update no exist parking zone");
		ParkingZone res = service.updateParkingZone(parkingDto3);
		assertNull(res);
	}
	
	@Test
	@Order(8)
	void addExistsZoneTest() {
		LOG.debug("***TEST-5 add exists parking zone");
		ParkingZone res = service.updateParkingZone(parkingDto1);
		assertNull(res);
	}

}
