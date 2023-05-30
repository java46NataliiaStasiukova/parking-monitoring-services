package parking.monitoring.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import parking.monitoring.entities.ParkingZone;

public interface ParkingZoneRepo extends MongoRepository<ParkingZone, String> {

}