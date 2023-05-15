package parking.monitoring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import parking.monitoring.entities.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {

	@Query(value = "select id, birthdate, email, name from drivers join cars on "
			+ "cars.driver_id = drivers.id where cars.number = :carNumber", nativeQuery = true)
	Driver findDriverByCarNumber(long carNumber);

}
