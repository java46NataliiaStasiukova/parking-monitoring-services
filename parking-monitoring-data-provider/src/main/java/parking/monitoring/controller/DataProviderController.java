package parking.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import parking.monitoring.NotificationData;
import parking.monitoring.service.DataProviderService;

@RestController
@RequestMapping("data")
public class DataProviderController {

	@Autowired
	DataProviderService dataProvider;
	
	@GetMapping("/{carNumber}")
	NotificationData getNotificationData(@PathVariable long carNumber) {
		return dataProvider.getNotificationData(carNumber);
	}
}
