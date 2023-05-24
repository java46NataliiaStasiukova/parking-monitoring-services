package parking.monitoring.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import parking.monitoring.CarPaymentData;
import parking.monitoring.ParkingFine;
import parking.monitoring.PaymentData;
import parking.monitoring.entities.LastCarPayment;
import parking.monitoring.repo.LastCarPaymentRepository;


@Service
public class PaymentCheckerServiceImpl implements PaymentCheckerService {

	static Logger LOG = LoggerFactory.getLogger(PaymentCheckerServiceImpl.class);
	
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
	public ParkingFine checkPayment(PaymentData payment) {
		ParkingFine res = null;
		LastCarPayment carPayment = null;
		CarPaymentData paymentData = getPaymentData(payment.carNumber, payment.parkingZone);
		if(paymentData == null || paymentData.status.equals("not-paid")) {
			LOG.warn("*payment-checker* parking not paid for car: {}", payment.carNumber);
			res = new ParkingFine(payment.carNumber, payment.parkingZone);
			carPayment = new LastCarPayment(payment.carNumber, payment.parkingZone, "not-paid", LocalDateTime.now());
			paymentRepository.save(carPayment);
		} else if(paymentData.status.equals("paid") && !isExpired(paymentData.paidTo)){
			LOG.debug("*payment-checker* parking paid and time not expired for car: {}", payment.carNumber);
			carPayment = new LastCarPayment(payment.carNumber, payment.parkingZone, "paid", paymentData.paidTo);
			paymentRepository.save(carPayment);
		} else if(!paymentData.parkingZone.equals(payment.parkingZone)) {
			LOG.debug("*payment-checker* parking zone changed for car car: {} from: {} to: {}", 
					payment.carNumber, payment.parkingZone, paymentData.parkingZone);
			res = new ParkingFine(payment.carNumber, payment.parkingZone);
			carPayment = new LastCarPayment(payment.carNumber, payment.parkingZone, paymentData.status, paymentData.paidTo);
			paymentRepository.save(carPayment);
		} else {
			LOG.debug("*payment-checker* time expired for car: {}", payment.carNumber);
			res = new ParkingFine(payment.carNumber, payment.parkingZone);
			carPayment = new LastCarPayment(payment.carNumber, payment.parkingZone, paymentData.status, paymentData.paidTo);
			paymentRepository.save(carPayment);
		}
		return res;
	}
	
	public CarPaymentData getPaymentData(long carNumber, String parkingZone) {
	CarPaymentData paymentData = null;
	try {
		ResponseEntity<CarPaymentData> response = restTemplate.exchange(getFullUrl(carNumber, parkingZone),
				HttpMethod.GET, null, CarPaymentData.class);
		paymentData =  response.getBody();
		LOG.debug("*payment-checker* from pango service received car's payment data: {}", paymentData.toString());
	} catch (RestClientException e) {
		LOG.error("*payment-checker* pango data provider has not sent the data; reason: {}",e.getMessage());
	}
	return paymentData;
	}

	private String getFullUrl(long carNumber, String parkingZone) {
		String url = String.format("http://%s:%s/%s/%s/%s", host,port,mappingUrl, carNumber, parkingZone);
		LOG.debug("*payment-checker* URL for communicating with pango data provider is {}", url);
		return url;
	}

	private boolean isExpired(LocalDateTime expiredTime) {
	
		return expiredTime.compareTo(LocalDateTime.now()) < 0;
	}

}
