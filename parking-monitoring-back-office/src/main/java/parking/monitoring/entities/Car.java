package parking.monitoring.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class Car {

	@Id
	long carNumber;

	@OneToOne
	@JoinColumn(name = "driver_id")
	Driver driver;

	public Car(long carNumber, Driver driver) {
		this.carNumber = carNumber;
		this.driver = driver;
	}

	public Car() {

	}

	public long getCarNumber() {
		return carNumber;
	}

	public Driver getDriver() {
		return driver;
	}

}
