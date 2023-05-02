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
	@Value("${app.amount.days:1}")
	int nDays;//after nDays the repository will be cleaned from all data
	static LocalDate timestamp = LocalDate.now().plusDays(1);
	
	@Override
	@Transactional
	public NewCarScan processCarScan(CarScan car) {
		if(timestamp.compareTo(LocalDate.now()) == 0) {
			timestamp = timestamp.plusDays(nDays);
			lastScanRepository.deleteAll();
			LOG.info("*car-analyzer all data from repository was deleted according to the expiry of timestamp");
		}
		NewCarScan res = null;
		LastScan lastScan = lastScanRepository.findById(car.carNumber).orElse(null);
		if(lastScan == null) {
			LOG.debug("*car-analyzer* new car with car number: {} on parking", car.carNumber);
			lastScan = new LastScan(car.carNumber, LocalDateTime.now().plusHours(1));
			lastScanRepository.save(lastScan);
		} else if (isExpired(lastScan)) {
			res = new NewCarScan(car.carNumber, car.parkingZone);
			lastScan.setExpiry(LocalDateTime.now());
			LOG.debug("*car-analyzer* time expired for car with car number: {}", car.carNumber);
		}
		//trace
		LOG.debug("*car-analyzer* last scan for car with car number: {} and expiry time: {}", lastScan.getCarNumber(), lastScan.getExpiry());
		
		return res;
	}

	private boolean isExpired(LastScan carScan) {
		
		return carScan.getExpiry().compareTo(LocalDateTime.now()) > 0;
	}

}
