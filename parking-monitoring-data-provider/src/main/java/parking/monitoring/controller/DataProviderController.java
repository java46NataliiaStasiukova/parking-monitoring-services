package parking.monitoring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import parking.monitoring.NotificationData;
import parking.monitoring.dto.ParkingZoneDto;
import parking.monitoring.service.DataProviderService;

@RestController
@RequestMapping("driver-data")
public class DataProviderController {
	
	static Logger LOG = LoggerFactory.getLogger(DataProviderController.class);

	@Autowired
	DataProviderService dataProvider;
	
	@GetMapping("/{carNumber}")
	NotificationData getNotificationData(@PathVariable long carNumber) {
		LOG.debug("*data-provider* request for getting notification data by car number: {}", carNumber);
		return dataProvider.getNotificationData(carNumber);
	}
	
	@GetMapping("/parkingZone/{parkingZone}")
	ParkingZoneDto getParkingZoneData(@PathVariable String parkingZone) {
		LOG.debug("*data-provider* request for getting parking data by parking zone: {}", parkingZone);
		return dataProvider.getParkingZoneData(parkingZone);
	}
}
