package parking.monitoring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import parking.monitoring.dto.DriverDto;
import parking.monitoring.entities.Car;
import parking.monitoring.entities.Driver;

public interface CarRepository extends JpaRepository<Car, Long> {

	Car findCarByDriverId(long driverId);

}
