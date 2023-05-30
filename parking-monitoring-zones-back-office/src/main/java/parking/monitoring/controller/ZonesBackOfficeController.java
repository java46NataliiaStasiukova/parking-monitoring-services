package parking.monitoring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import parking.monitoring.dto.ParkingZoneDto;
import parking.monitoring.entities.ParkingZone;
import parking.monitoring.service.ParkingZonesService;


@RestController
@RequestMapping("zones")
@Validated
public class ZonesBackOfficeController {

	static Logger LOG = LoggerFactory.getLogger(ZonesBackOfficeController.class);
	@Autowired
	ParkingZonesService service;
	
	@PostMapping
	String addParkingZone(@RequestBody @Valid ParkingZoneDto dto) {
		LOG.debug("*zones-back-office* request for adding new parking zone");
		String res = String.format("parking zone with id %s already exist", dto.parkingZone);
		ParkingZone zone = service.addParkingZone(dto);
		if(zone != null) {
			res = String.format("new parking zone: %s was added", dto.toString());
		}
		return res;
	}
	
	@DeleteMapping("/{parkingZone}")
	String deleteParkingZone(@PathVariable String parkingZone) {
		LOG.debug("*zones-back-office* request for deleting parking zone with id: {}", parkingZone);
		String res = String.format("parking zone with id %s doesn't exist", parkingZone);
		ParkingZone zone = service.deleteParkingZone(parkingZone);
		if(zone != null) {
			res = String.format("parking zone with id: %s was deleted", parkingZone);
		}
		return res;
	}
	
	@GetMapping("/{parkingZone}")
	String getParkingZone(@PathVariable String parkingZone) {
		LOG.debug("*zones-back-office* request for getting parking zone by id: {}", parkingZone);
		String res = String.format("parking zone with id: %s doesn't exist", parkingZone);
		ParkingZone zone = service.getParkingZone(parkingZone);
		if(zone != null) {
			res = String.format("found parking zone: %s", zone.toString());
		}
		return res;
	}
	
	@PutMapping
	String updateParkingZone(@RequestBody @Valid ParkingZoneDto dto) {
		LOG.debug("*zones-back-office* request for updating parking zone with id: {}", dto.parkingZone);
		String res = String.format("parking zone with id: %s doesn't exust", dto.parkingZone);
		ParkingZone zone = service.updateParkingZone(dto);
		if(zone != null) {
			res = String.format("parking zone with id: %s was updated", dto.parkingZone);
		}
		return res;
	}

}
