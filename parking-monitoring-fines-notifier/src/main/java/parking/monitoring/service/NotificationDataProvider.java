package parking.monitoring.service;

import parking.monitoring.NotificationData;
import parking.monitoring.dto.ParkingZoneDto;

public interface NotificationDataProvider {

	NotificationData getNotificationData(long carNumber);
	
	ParkingZoneDto getParkingZoneData(String parkingZone);
	
}
