package parking.monitoring.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import parking.monitoring.CarPaymentData;

@Service
public class PangoDataProviderServiceImpl implements PangoDataProviderService {

	static Logger LOG = LoggerFactory.getLogger(PangoDataProviderServiceImpl.class);
	@Autowired
	private RestTemplate restTemplate;
	 @Value("${app.pango.data.provider.mapping.url:pango}")
	 String mappingUrl;
	 @Value("${app.pango.data.provider.host:localhost}")
	 String host;
	 @Value("${app.pango.data.provider.port:8585}")
	 int port;
	
	@Override
	public CarPaymentData getCarPaymentData(long carNumber, String parkingZone) {
			CarPaymentData carPaymentData = null;
		try {
			ResponseEntity<CarPaymentData> response =
					restTemplate.exchange(getFullUrl(carNumber, parkingZone),
							HttpMethod.GET, null, CarPaymentData.class);
			carPaymentData =  response.getBody();
			LOG.debug("*pango-data-provider* received payment data from pango service: {}, {}", carPaymentData.toString());
		} catch (RestClientException e) {
			LOG.error("*pango-data-provider* Pango service has not sent the data; reason: {}",e.getMessage());
		}
		return carPaymentData;
	}
	
	private String getFullUrl(long patientId, String parkingZone) {
		String url = String.format("http://%s:%s/%s/%s/%s", host, port, mappingUrl, patientId, parkingZone);
		LOG.debug("URL for communicating with pango servise is {}", url);
		return url;
	}

}
