package parking.monitoring.service;

import java.time.LocalDateTime;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import parking.monitoring.NewCarScan;
import parking.monitoring.ParkingFine;
import parking.monitoring.PaymentData;
import parking.monitoring.entities.LastCarPayment;
import parking.monitoring.repo.LastCarPaymentRepository;

@Service
public class FinesAnalyzerServiceImpl implements FinesAnalyzerService {
	static Logger LOG = LoggerFactory.getLogger(FinesAnalyzerServiceImpl.class);
	
	 @Autowired
	 LastCarPaymentRepository paymentRepository;
	
	@Override
	@Transactional
	public ParkingFine processNewCarScan(NewCarScan car) {
		ParkingFine res = null;
		LastCarPayment carPayment = paymentRepository.findById(car.carNumber).orElse(null);
		if(carPayment == null) {
			LOG.debug("*fines-analyzer* no payment information for car : {}", car.carNumber);
		} else {
			if(carPayment.getStatus().equals("paid") && isExpired(carPayment.getPaidTill())) {
				LOG.debug("*fines-analyzer* parking payment is expired for car: {}", car.carNumber);
				res = new ParkingFine(car.carNumber, car.parkingZone);
				carPayment = new LastCarPayment(car.carNumber, car.parkingZone, "not-paid", carPayment.getPaidTill());
				paymentRepository.save(carPayment);
			} else if(carPayment.getStatus().equals("not-paid")) {
				LOG.debug("*fines-analyzer* parking status is 'not-paid' for car: {}", car.carNumber);
				res = new ParkingFine(car.carNumber, car.parkingZone);
			} else if(!carPayment.getParkingZone().equals(car.parkingZone)) {
				LOG.debug("*fines-analyzer* parking zone changed for car car: {} from: {} to: {}", 
						car.carNumber, car.parkingZone, carPayment.getParkingZone());
				res = new ParkingFine(car.carNumber, car.parkingZone);
				carPayment = new LastCarPayment(car.carNumber, car.parkingZone, carPayment.getStatus(), carPayment.getPaidTill());
				paymentRepository.save(carPayment);
			}
			LOG.debug("*fines-analyzer* last car payment status: {} for car: {}, till: {}", carPayment.getStatus(), car.carNumber, carPayment.getPaidTill());
		}
		return res;
	}

	private boolean isExpired(LocalDateTime expiredTime) {
		
		return expiredTime.compareTo(LocalDateTime.now()) < 0;
	}

	@Override
	public PaymentData checkNewCarScan(NewCarScan car) {
		PaymentData res = null;
		LastCarPayment carPayment = paymentRepository.findById(car.carNumber).orElse(null);
		if(carPayment == null) {
			LOG.debug("*fines-analyzer* no payment data for car : {}", car.carNumber);
			res = new PaymentData(car.carNumber, car.parkingZone);
		}
		return res;
	}

}
