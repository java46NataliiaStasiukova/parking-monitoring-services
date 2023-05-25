package parking.monitoring.service;

public interface GarbageCollectorService {

	void cleanCarFineRepo();
	
	void cleanCarPaymentRepo();
	
	void cleanCarScanRepo();
	
}
