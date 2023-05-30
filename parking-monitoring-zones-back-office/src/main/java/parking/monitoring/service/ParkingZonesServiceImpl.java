package parking.monitoring.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import parking.monitoring.dto.ParkingZoneDto;
import parking.monitoring.entities.ParkingZone;
import parking.monitoring.repo.ParkingZoneRepo;


@Service
@Transactional
public class ParkingZonesServiceImpl implements ParkingZonesService {

	static Logger LOG = LoggerFactory.getLogger(ParkingZonesServiceImpl.class);
	@Autowired
	ParkingZoneRepo parkingZoneRepo;
	
	@Override
	public ParkingZone addParkingZone(ParkingZoneDto parkingDto) {
		ParkingZone res = null;
		if(!parkingZoneRepo.existsById(parkingDto.parkingZone)){
			LOG.debug("*zones-back-office* adding new parking zone: {}", parkingDto.toString());
			res = ParkingZone.of(parkingDto);
			parkingZoneRepo.save(res);
		} else {
			LOG.debug("*zones-back-office* parking zone with id: {} already exist", parkingDto.parkingZone);
		}
		return res;
	}

	@Override
	public ParkingZone getParkingZone(String parkingZone) {
		LOG.debug("*zones-back-office* getting parking zone by id: {}", parkingZone);
		ParkingZone res = parkingZoneRepo.findById(parkingZone).orElse(null);
		return res;
	}

	@Override
	public ParkingZone deleteParkingZone(String parkingZone) {
		LOG.debug("*zones-back-office* deleting parking zone with id: {}", parkingZone);
		ParkingZone res = null;
		if(parkingZoneRepo.existsById(parkingZone)) {
			res = parkingZoneRepo.findById(parkingZone).orElse(null);
			parkingZoneRepo.deleteById(parkingZone);
		}
		return res;
	}

	@Override
	public ParkingZone updateParkingZone(ParkingZoneDto parkingDto) {
		LOG.debug("*zones-back-office* updating parking zone with id: {}", parkingDto.parkingZone);
		ParkingZone res = parkingZoneRepo.findById(parkingDto.parkingZone).orElse(null);
		if(res != null) {
			res = ParkingZone.of(parkingDto);
			parkingZoneRepo.save(res);
		}
		return res;
	}

}
