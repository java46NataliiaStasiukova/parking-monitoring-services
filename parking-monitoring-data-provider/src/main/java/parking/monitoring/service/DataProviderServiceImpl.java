package parking.monitoring.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import parking.monitoring.NotificationData;
import parking.monitoring.dto.ParkingZoneDto;
import parking.monitoring.entities.*;
import parking.monitoring.repo.*;


@Service
public class DataProviderServiceImpl implements DataProviderService {
	static Logger LOG = LoggerFactory.getLogger(DataProviderServiceImpl.class);
	@Autowired
	CarRepository carRepository;
	@Autowired
	DriverRepository driverRepository;
	@Autowired
	ParkingZoneRepo parkingZoneRepo;
	
	@Override
	public NotificationData getNotificationData(long carNumber) {
		Car car = carRepository.findById(carNumber).orElse(null);
		if(car == null) {
			LOG.warn("*data-provider* car: {} not exist", carNumber);
			throw new IllegalStateException(String.format("Car with number: %s not exist",
					carNumber));
		}
		Driver driver = driverRepository.findById(car.getDriver().getId()).orElse(null);
		if(driver == null) {
			LOG.warn("*data-provider* driver for car: {} not exist", carNumber);
			throw new IllegalStateException(String.format("Driver for car with number: %s not exist",
					carNumber));
		}
		NotificationData data = new NotificationData(carNumber, driver.getEmail(), 
				driver.getName(), driver.getId());
		LOG.debug("*data-provider* recived notification data for driver: {}", data.toString());
		return data;
	}

	@Override
	public ParkingZoneDto getParkingZoneData(String parkingZone) {
		ParkingZone zone = parkingZoneRepo.findById(parkingZone).orElse(null);
		if(zone == null) {
			LOG.warn("*data-provider* parking zone: {} not exist", parkingZone);
			throw new IllegalStateException(String.format("Parking zone with id: %s not exist",
					parkingZone));
		}
		ParkingZoneDto parkingDto = new ParkingZoneDto(zone.getParkingZone(), zone.getFineCost(), 
				zone.getCity(), zone.getAddres());
		LOG.debug("*data-provider* recived parking zone data: {}", parkingDto.toString());
		return parkingDto;
	}

}
