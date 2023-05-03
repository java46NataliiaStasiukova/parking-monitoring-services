package parking.monitoring.service;

import java.time.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import parking.monitoring.CarScan;
import parking.monitoring.NewCarScan;
import parking.monitoring.entities.LastScan;
import parking.monitoring.repo.LastScanRepository;

@Service
public class CarAnalyzerServiceImpl implements CarAnalyzerService {
	
	@Autowired
	LastScanRepository lastScanRepository;
	static Logger LOG = LoggerFactory.getLogger(CarAnalyzerServiceImpl.class);

	@Override
	@Transactional
	public NewCarScan processCarScan(CarScan car) {
		NewCarScan res = null;
		LastScan lastScan = lastScanRepository.findById(car.carNumber).orElse(null);
		if(lastScan == null) {
			LOG.debug("*car-analyzer* new car with car number: {} on parking", car.carNumber);
			lastScan = new LastScan(car.carNumber, LocalDateTime.now().plusHours(1));
			lastScanRepository.save(lastScan);
			res = new NewCarScan(car.carNumber, car.parkingZone);
		} else if (isExpired(lastScan)) {
			LOG.debug("*car-analyzer* time expired for car with car number: {}", car.carNumber);
			lastScanRepository.save(lastScan);
			res = new NewCarScan(car.carNumber, car.parkingZone);
		}
		//trace
		LOG.debug("*car-analyzer* last scan for car with car number: {} and expiry time: {}", lastScan.getCarNumber(), lastScan.getExpiry());
		
		return res;
	}

	private boolean isExpired(LastScan carScan) {
		
		return carScan.getExpiry().compareTo(LocalDateTime.now()) < 0;
	}

}
