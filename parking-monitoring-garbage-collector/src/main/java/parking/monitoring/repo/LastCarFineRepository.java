package parking.monitoring.repo;

import org.springframework.data.repository.CrudRepository;

import parking.monitoring.entities.LastCarFine;

public interface LastCarFineRepository extends CrudRepository<LastCarFine, Long> {

}

