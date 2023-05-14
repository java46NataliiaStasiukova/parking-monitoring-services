package parking.monitoring.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import parking.monitoring.entities.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

	Car findCarByDriverId(long driverId);

}
