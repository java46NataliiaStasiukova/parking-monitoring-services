package parking.monitoring.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import parking.monitoring.CarPaymentData;

@Service
public class PangoDataProviderServiceImpl implements PangoDataProviderService {

	static Logger LOG = LoggerFactory.getLogger(PangoDataProviderServiceImpl.class);
	@Autowired
	PangoImitatorService pangoService;
	
	@Override
	public CarPaymentData getCarPaymentData(long carNumber, String parkingZone) {
		LOG.debug("*pango-data-provider* car number: {}", carNumber);
		return pangoService.getData(carNumber, parkingZone);
	}

}
