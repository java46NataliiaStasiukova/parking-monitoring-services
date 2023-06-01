package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import parking.monitoring.dto.ParkingZoneDto;
import parking.monitoring.entities.ParkingZone;
import parking.monitoring.repo.ParkingZoneRepo;
import parking.monitoring.service.DataProviderService;


@SpringBootTest
class DataProviderMongoTest {
	
	@Autowired
	ParkingZoneRepo parkingRepo;
	@Autowired
	DataProviderService service;
	
	ParkingZoneDto zone1 = new ParkingZoneDto("1", 200.0, "Bat Yam", "Yerushalaim");
	ParkingZoneDto zone2 = new ParkingZoneDto("2", 400.0, "Holon", "Dov Hoz");
	ParkingZoneDto zone3 = new ParkingZoneDto("3", 600.0, "Tel Aviv", "Hayarkon");
	
	@BeforeEach
	void addParkingZone() {
		parkingRepo.save(ParkingZone.of(zone1));
		parkingRepo.save(ParkingZone.of(zone2));
		parkingRepo.save(ParkingZone.of(zone3));
	}

	@Test
	void getParkingDataTest() {
		assertEquals(zone1, service.getParkingZoneData("1"));
		assertEquals(zone2, service.getParkingZoneData("2"));
		assertEquals(zone3, service.getParkingZoneData("3"));
	}

}
