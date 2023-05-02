package parking.monitoring.service;

import parking.monitoring.*;

public interface CarAnalyzerService {
	
	NewCarScan processCarScan(CarScan car);
}
