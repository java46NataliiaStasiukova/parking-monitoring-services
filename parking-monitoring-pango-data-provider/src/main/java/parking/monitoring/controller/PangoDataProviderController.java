package parking.monitoring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import parking.monitoring.CarPaymentData;
import parking.monitoring.service.PangoDataProviderService;


@RestController
@RequestMapping("paymentData")
public class PangoDataProviderController {

	static Logger LOG = LoggerFactory.getLogger(PangoDataProviderController.class);
	@Autowired
	PangoDataProviderService pangoDataProvider;
	
	@GetMapping("/{carNumber}/{parkingZone}")
	CarPaymentData getCarPaymentData(@PathVariable long carNumber, @PathVariable String parkingZone) {
		LOG.debug("*pango-data-provider* request for getting payment data for car: {} in parking zone: {}", 
				carNumber, parkingZone);
		return pangoDataProvider.getCarPaymentData(carNumber, parkingZone);
	}
	
}
