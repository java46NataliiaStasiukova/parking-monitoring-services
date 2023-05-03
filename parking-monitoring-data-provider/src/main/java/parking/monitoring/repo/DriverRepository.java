package parking.monitoring.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import parking.monitoring.entities.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {

}
