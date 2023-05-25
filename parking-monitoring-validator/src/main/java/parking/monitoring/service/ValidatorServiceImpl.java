package parking.monitoring.service;

import java.time.LocalDate;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import parking.monitoring.ParkingFine;
import parking.monitoring.ParkingReport;
import parking.monitoring.entities.LastCarFine;
import parking.monitoring.repo.LastCarFineRepository;

@Service
public class ValidatorServiceImpl implements ValidatorService {

	static Logger LOG = LoggerFactory.getLogger(ValidatorServiceImpl.class);
	@Autowired
	LastCarFineRepository fineRepository;
	
	@Override
	@Transactional
	public ParkingReport validate(ParkingFine fine) {
		ParkingReport res = null;
		LastCarFine lastFine = fineRepository.findById(fine.carNumber).orElse(null);
		if(lastFine == null) {
			LOG.debug("*validator* new report for car with number: {}", fine.carNumber);
			lastFine = new LastCarFine(fine.carNumber, fine.parkingZone, LocalDate.now());
			fineRepository.save(lastFine);
			res = new ParkingReport(fine.carNumber, fine.parkingZone);
		} else {
			if(!fine.parkingZone.equals(lastFine.getParkingZone())) {
				LOG.debug("*validator* car changed parking zone, new report for car with number: {}", fine.carNumber);
				lastFine.setParkingZone(fine.parkingZone);
				fineRepository.save(lastFine);
				res = new ParkingReport(fine.carNumber, fine.parkingZone);
			} else if(!lastFine.getDate().equals(LocalDate.now())) {
				LOG.debug("*validator* car: {} reciving new report", fine.carNumber);
				lastFine = new LastCarFine(fine.carNumber, fine.parkingZone, LocalDate.now());
				fineRepository.save(lastFine);
				res = new ParkingReport(fine.carNumber, fine.parkingZone);
			}
		}
		
		LOG.debug("*validator* last fine for car with number: {}, and parking zone: {}", fine.carNumber, fine.parkingZone);
		
		return res;
	}

}
