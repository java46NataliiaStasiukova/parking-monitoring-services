package parking.monitoring.service;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import parking.monitoring.dto.ParkingZoneDto;
import parking.monitoring.entities.ParkingZone;
import parking.monitoring.repo.ParkingZoneRepo;


@Service
@Transactional
public class ParkingZonesServiceImpl implements ParkingZonesService {

	static Logger LOG = LoggerFactory.getLogger(ParkingZonesServiceImpl.class);
//	@Autowired
//	MongoTemplate mongoTemplate;
	@Autowired
	ParkingZoneRepo parkingZoneRepo;
	
	@Override
	public ParkingZone addParkingZone(ParkingZoneDto parkingZoneDto) {
		ParkingZone doc = null;
		if(!parkingZoneRepo.existsById(parkingZoneDto.parkingZone)){
			LOG.debug("*zones-back-office* adding new parking zone: {}", parkingZoneDto.toString());
			doc = ParkingZone.of(parkingZoneDto);
			parkingZoneRepo.save(doc);
		} else {
			LOG.debug("*zones-back-office* parking zone with id: {} already exist", parkingZoneDto.parkingZone);
		}
		return doc;
	}

	@Override
	public ParkingZone getParkingZone(String parkingZone) {
		LOG.debug("*zones-back-office* getting parking zone by id: {}", parkingZone);
		ParkingZone doc = null;
		if(parkingZoneRepo.existsById(parkingZone)) {
			
		}
		return null;
	}

	@Override
	public ParkingZone deleteParkingZone(String parkingZone) {
		LOG.debug("*zones-back-office* getting parking zone by id: {}", parkingZone);
		ParkingZone doc = null;
		if(parkingZoneRepo.existsById(parkingZone)) {
			//doc = parkingZoneRepo.
			//parkingZoneRepo.deleteById(parkingZone);
		}
		return doc;
	}

}
