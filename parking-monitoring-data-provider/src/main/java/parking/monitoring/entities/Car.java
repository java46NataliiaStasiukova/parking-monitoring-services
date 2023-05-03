package parking.monitoring.entities;

import jakarta.persistence.*;

@Entity
@Table(name="cars")
public class Car {

	@Id
	long carNumber;
	long driverId;
	
	public Car(long carNumber, long driverId) {
		this.carNumber = carNumber;
		this.driverId = driverId;
	}

	public Car() {
		
	}




}
