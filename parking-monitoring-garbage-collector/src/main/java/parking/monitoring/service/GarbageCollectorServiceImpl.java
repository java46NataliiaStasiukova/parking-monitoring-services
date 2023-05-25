package parking.monitoring.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import parking.monitoring.repo.*;

@Service
public class GarbageCollectorServiceImpl implements GarbageCollectorService {

	static Logger LOG = LoggerFactory.getLogger(GarbageCollectorService.class);
	@Autowired
	LastCarFineRepository carFineRepo;
	@Autowired
	LastCarPaymentRepository carPaymentRepo;
	@Autowired
	LastScanRepository carScanRepo;
	
	@Override
	@Scheduled(fixedDelay = 3600000)
	public void cleanCarFineRepo() {
		carFineRepo.deleteAll();
		LOG.debug("*garbage-collector* CarFineRepository was cleaned from all data");
		LOG.debug("*garbage-collector* next clean-up for CarFineRepository in: 1 hour");
	}

	@Override
	@Scheduled(fixedDelay = 3600000)
	public void cleanCarPaymentRepo() {
		carPaymentRepo.deleteAll();
		LOG.debug("*garbage-collector* CarPaymentRepository was cleaned from all data");
		LOG.debug("*garbage-collector* next clean-up for CarPaymentRepository in: 1 hour");
	}

	@Override
	@Scheduled(fixedDelay = 3600000)
	public void cleanCarScanRepo() {
		carScanRepo.deleteAll();
		LOG.debug("*garbage-collector* CarScanRepository was cleaned from all data");
		LOG.debug("*garbage-collector* next clean-up for CarScanRepository in: 1 hour");
	}

}
