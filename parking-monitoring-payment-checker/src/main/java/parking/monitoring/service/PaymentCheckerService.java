package parking.monitoring.service;

import parking.monitoring.ParkingFine;
import parking.monitoring.PaymentData;

public interface PaymentCheckerService {
	
	ParkingFine checkPayment(PaymentData payment);

}
