package parking.monitoring.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import parking.monitoring.NotificationData;
import parking.monitoring.dto.ParkingZoneDto;

@Service
public class NotificationDataProviderImpl implements NotificationDataProvider {

	static private Logger LOG = LoggerFactory.getLogger(NotificationDataProviderImpl.class);
	@Autowired
	private RestTemplate restTemplate;
	 @Value("${app.notification.data.provider.mapping.url:driver-data}")
	 String mappingUrl;
	 @Value("${app.parking.zone.data.mapping.url:driver-data/parkingZone}")
	 String mappingUrl_2;
	 @Value("${app.data.provider.host:localhost}")
	 String host;
	 @Value("${app.data.provider.port:8989}")
	 int port;
	
	@Override
	public NotificationData getNotificationData(long carNumber) {
		NotificationData notificationData = null;
		try {
			ResponseEntity<NotificationData> response =
					restTemplate.exchange(getFullUrl(carNumber),
							HttpMethod.GET, null, NotificationData.class);
			notificationData =  response.getBody();
			LOG.debug("*notifier* notification data received from data provider: {}", notificationData.toString());
		} catch (RestClientException e) {
			LOG.error("Notification Data Provider has not sent the data; reason: {}",e.getMessage());
		}
		return notificationData;
	}
	
	private String getFullUrl(long patientId) {
		String url = String.format("http://%s:%s/%s/%s", host, port, mappingUrl, patientId);
		LOG.debug("*notifier* URL for communicating with data provider is {}", url);
		return url;
	}
	
	@Override
	public ParkingZoneDto getParkingZoneData(String parkingZone) {
		ParkingZoneDto parkingDto = null;
		try {
			ResponseEntity<ParkingZoneDto> response =
					restTemplate.exchange(getFullUrlParking(parkingZone),
							HttpMethod.GET, null, ParkingZoneDto.class);
			parkingDto =  response.getBody();
			LOG.debug("*notifier* parking zone data received from data provider: {}", parkingDto.toString());
		} catch (RestClientException e) {
			LOG.error("Data Provider has not sent parking zone data; reason: {}",e.getMessage());
		}
		return parkingDto;
	}
	
	private String getFullUrlParking(String parkingZone) {
		String url = String.format("http://%s:%s/%s/%s", host, port, mappingUrl_2, parkingZone);
		LOG.debug("*notifier* URL for communicating with data provider is {}", url);
		return url;
	}

}
