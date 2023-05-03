package parking.monitoring.service;

import parking.monitoring.NotificationData;

public interface DataProviderService {
	
	NotificationData getNotificationData(long carNumber);
}
