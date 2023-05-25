package parking.monitoring.service;

import parking.monitoring.NewCarScan;
import parking.monitoring.ParkingFine;
import parking.monitoring.PaymentData;

public interface PaymentCheckerService {
	
	NewCarScan checkPayment(PaymentData payment);

}
