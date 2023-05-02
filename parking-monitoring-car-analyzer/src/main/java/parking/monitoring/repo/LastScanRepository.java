package parking.monitoring.repo;

import org.springframework.data.repository.CrudRepository;

import parking.monitoring.entities.LastScan;

public interface LastScanRepository extends CrudRepository<LastScan, Long> {

}
