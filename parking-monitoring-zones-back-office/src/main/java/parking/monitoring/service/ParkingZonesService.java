package parking.monitoring.service;

import parking.monitoring.dto.ParkingZoneDto;
import parking.monitoring.entities.ParkingZone;

public interface ParkingZonesService {
	
	ParkingZone addParkingZone(ParkingZoneDto parkingZoneDto);
	
	ParkingZone getParkingZone(String parkingZone);
	
	ParkingZone deleteParkingZone(String parkingZone);
	
}
