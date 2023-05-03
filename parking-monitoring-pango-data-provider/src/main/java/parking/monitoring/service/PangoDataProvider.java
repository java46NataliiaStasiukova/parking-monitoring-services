package parking.monitoring.service;

import parking.monitoring.CarPaymentData;

public interface PangoDataProvider {
	
	CarPaymentData getPaymentData(long carNumber);

}
