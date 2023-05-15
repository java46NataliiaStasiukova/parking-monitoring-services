package parking.monitoring.service;

import parking.monitoring.ParkingFine;
import parking.monitoring.ParkingReport;

public interface ValidatorService {
	
	ParkingReport validate(ParkingFine fine);

}
