package parking.monitoring.service;

import parking.monitoring.CarPaymentData;

public interface PangoDataProviderService {
	
	CarPaymentData getCarPaymentData(long carNumber);

}
