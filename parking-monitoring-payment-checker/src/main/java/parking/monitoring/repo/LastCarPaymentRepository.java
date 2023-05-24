package parking.monitoring.repo;

import org.springframework.data.repository.CrudRepository;

import parking.monitoring.entities.LastCarPayment;

public interface LastCarPaymentRepository extends CrudRepository<LastCarPayment, Long> {

}