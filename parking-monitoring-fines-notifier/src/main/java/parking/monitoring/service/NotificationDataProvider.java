package parking.monitoring.service;

import parking.monitoring.NotificationData;

public interface NotificationDataProvider {

	NotificationData getNotificationData(long carNumber);
	
}
