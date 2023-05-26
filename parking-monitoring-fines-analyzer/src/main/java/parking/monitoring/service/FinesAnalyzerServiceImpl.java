package parking.monitoring.service;

import java.time.LocalDateTime;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import parking.monitoring.CarPaymentData;
import parking.monitoring.NewCarScan;
import parking.monitoring.ParkingFine;
import parking.monitoring.entities.LastCarPayment;
import parking.monitoring.repo.LastCarPaymentRepository;

@Service
public class FinesAnalyzerServiceImpl implements FinesAnalyzerService {
	static Logger LOG = LoggerFactory.getLogger(FinesAnalyzerServiceImpl.class);
	
	 @Autowired
	 LastCarPaymentRepository paymentRepository;
	 @Autowired
	 private RestTemplate restTemplate;
	 @Value("${app.data.provider.mapping.url:paymentData}")
	 String mappingUrl;
	 @Value("${app.data.provider.host:localhost}")
	 String host;
	 @Value("${app.data.provider.port:8181}")
	 int port;
	
	@Override
	@Transactional
	public ParkingFine processNewCarScan(NewCarScan car) {
		ParkingFine res = null;
		CarPaymentData paymentData = null;
		LastCarPayment carPayment = paymentRepository.findById(car.carNumber).orElse(null);
		if(carPayment == null) {
			LOG.debug("*fines-analyzer* no payment information for car : {}", car.carNumber);
			paymentData = getPaymentData(car.carNumber, car.parkingZone);
			if(paymentData == null) {
				carPayment = new LastCarPayment(car.carNumber, car.parkingZone, "not-paid", LocalDateTime.now());
				paymentRepository.save(carPayment);
				return new ParkingFine(car.carNumber, car.parkingZone);
			} else {
				carPayment = new LastCarPayment(car.carNumber, paymentData.parkingZone, paymentData.status, paymentData.paidTo);
				paymentRepository.save(carPayment);
			}
		}
		if(isExpired(carPayment.getPaidTill())) {
			LOG.debug("*fines-analyzer* parking payment is expired for car: {}", car.carNumber);
			res = new ParkingFine(car.carNumber, car.parkingZone);
		} else if(carPayment.getStatus().equals("not-paid")) {
			LOG.debug("*fines-analyzer* parking status is 'not-paid' for car: {}", car.carNumber);
			res = new ParkingFine(car.carNumber, car.parkingZone);
		} else if(!carPayment.getParkingZone().equals(car.parkingZone)) {
			LOG.debug("*fines-analyzer* parking zone changed for car car: {} from: {} to: {}", 
					car.carNumber, car.parkingZone, carPayment.getParkingZone());
			res = new ParkingFine(car.carNumber, car.parkingZone);
		}
		LOG.debug("*fines-analyzer* last car payment for car: {}, parking zone: {}, paid till: {}", carPayment.getCarNumber(),
				carPayment.getParkingZone(), carPayment.getPaidTill());
		return res;
	}

	private boolean isExpired(LocalDateTime expiredTime) {
		
		return expiredTime.compareTo(LocalDateTime.now()) < 0;
	}
	
	private CarPaymentData getPaymentData(long carNumber, String parkingZone) {
		CarPaymentData paymentData = null;
		try {
			ResponseEntity<CarPaymentData> response = restTemplate.exchange(getFullUrl(carNumber, parkingZone),
					HttpMethod.GET, null, CarPaymentData.class);
			paymentData =  response.getBody();
			LOG.debug("*fines-analyzer* from pango service received car's payment data: {}", paymentData.toString());
		} catch (RestClientException e) {
			LOG.error("pango data provider has not sent the data; reason: {}",e.getMessage());
		}
		return paymentData;
	}
	
	private String getFullUrl(long carNumber, String parkingZone) {
		String url = String.format("http://%s:%s/%s/%s/%s", host,port,mappingUrl, carNumber, parkingZone);
		LOG.debug("*fines-analyzer* URL for communicating with pango data provider is {}", url);
		return url;
	}

}
