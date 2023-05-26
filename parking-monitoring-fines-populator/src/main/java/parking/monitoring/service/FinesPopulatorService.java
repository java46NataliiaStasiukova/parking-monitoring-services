package parking.monitoring.service;

import parking.monitoring.dto.ReportDto;

public interface FinesPopulatorService {
	
	void addReport(ReportDto report);

}
