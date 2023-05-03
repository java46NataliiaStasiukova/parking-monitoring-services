package parking.monitoring.service;

import java.net.URI;

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
public class PangoDataProviderImpl implements PangoDataProvider {
	static private Logger LOG = LoggerFactory.getLogger(PangoDataProviderImpl.class);
	@Autowired
	private RestTemplate restTemplate;
	
	 @Value("${app.pango.data.provider.mapping.url:paymentData}")
	 String mappingUrl;
	 @Value("${app.pango.data.provider.host:localhost}")
	 String host;
	 @Value("${app.pango.data.provider.port:8080}")
	 int port;
	
	

	@Override
	public CarPaymentData getPaymentData(long carNumber) {
		CarPaymentData paymentData = null;
		try {
			ResponseEntity<CarPaymentData> response =
					restTemplate.exchange(getFullUrl(carNumber),
							HttpMethod.GET, null, CarPaymentData.class);
			paymentData =  response.getBody();
			LOG.debug("*pango-data-provider* car status received from pango: {}", paymentData.status);
		} catch (RestClientException e) {
			LOG.error("*pango-data-provider* Pango Service has not received payment data; reason: {}",e.getMessage());
		}
		return paymentData;
	}



	private String getFullUrl(long carNumber) {
		String url = String.format("http://%s:%s/%s/%s", host, port, mappingUrl, carNumber);
		LOG.debug("*pango-data-provider* URL for communication with pango service is: {}", url);
		return url;
	}

}
