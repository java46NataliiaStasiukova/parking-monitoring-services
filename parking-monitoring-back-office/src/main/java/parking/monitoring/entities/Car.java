package parking.monitoring.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class Car {

	@Id
	long number;

	@OneToOne
	@JoinColumn(name = "driver_id")
	Driver driver;
	
//	@OneToMany(mappedBy = "car", cascade = CascadeType.REMOVE)
//	List<Report> reports;

	public Car(long number, Driver driver) {
		this.number = number;
		this.driver = driver;
	}

	public Car() {

	}

	public long getNumber() {
		return number;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

}
