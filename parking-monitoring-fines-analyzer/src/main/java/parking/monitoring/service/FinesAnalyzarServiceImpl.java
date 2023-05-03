package parking.monitoring.service;

import java.time.LocalDateTime;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import parking.monitoring.CarPaymentData;
import parking.monitoring.NewCarScan;
import parking.monitoring.ParkingFine;
import parking.monitoring.entities.LastCarPayment;
import parking.monitoring.repo.LastCarPaymentRepository;

@Service
public class FinesAnalyzarServiceImpl implements FinesAnalyzerService {
	static Logger LOG = LoggerFactory.getLogger(FinesAnalyzarServiceImpl.class);
	@Autowired
	LastCarPaymentRepository paymentRepository;
	@Autowired
	PangoDataProvider dataProvider;
	
	@Override
	public ParkingFine processNewCarScan(NewCarScan car) {
		ParkingFine res = null;
		LastCarPayment carPayment = paymentRepository.findById(car.carNumber).orElse(null);
		if(carPayment == null) {
			LOG.debug("*fines-analyzer* payment stutus undefined for car with car number: {}", car.carNumber);
			CarPaymentData paymentData = dataProvider.getPaymentData(car.carNumber);
			if(paymentData == null || paymentData.status.equals("not-paid")) {
				LOG.warn("*fines-analyzer* parking not paid for car: {}", car.carNumber);
				res = new ParkingFine(car.carNumber, car.parkingZone, LocalDateTime.now());
				carPayment = new LastCarPayment(car.carNumber, "not-paid", LocalDateTime.now());
				paymentRepository.save(carPayment);
			} else {
				carPayment = new LastCarPayment(car.carNumber, "paid", paymentData.paidTo);
				paymentRepository.save(carPayment);
			}	
		} else if(carPayment.getStatus().equals("paid") && carPayment.getPaidTill().compareTo(LocalDateTime.now()) > 0) {
			LOG.debug("*fines-analyzer* parking payment is expired for car: {}", car.carNumber);
			res = new ParkingFine(car.carNumber, car.parkingZone, LocalDateTime.now());
			carPayment = new LastCarPayment(car.carNumber, "not-paid", carPayment.getPaidTill());
			paymentRepository.save(carPayment);
		} else if(carPayment.getStatus().equals("not-paid")) {
			LOG.debug("*fines-analyzer* parking status is 'not-paid' for car: {}", car.carNumber);
			res = new ParkingFine(car.carNumber, car.parkingZone, LocalDateTime.now());
		}
		//trace
		LOG.debug("*fines-analyzer* new parking fine: {} for car: {}", res.toString(), car.carNumber);
		
		return res;
	}

}
