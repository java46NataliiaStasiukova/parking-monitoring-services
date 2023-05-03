package parking.monitoring.service;

import parking.monitoring.*;

public interface FinesAnalyzerService {
	
	ParkingFine processNewCarScan(NewCarScan car);

}
