package parking.monitoring.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import parking.monitoring.NotificationData;

import parking.monitoring.repo.*;


@Service
public class DataProviderServiceImpl implements DataProviderService {
	static Logger LOG = LoggerFactory.getLogger(DataProviderServiceImpl.class);
	@Autowired
	CarRepository carRepository;
	@Autowired
	DriverRepository driverRepository;
	
	
	@Override
	public NotificationData getNotificationData(long carNumber) {

		
		return null;
	}

}
