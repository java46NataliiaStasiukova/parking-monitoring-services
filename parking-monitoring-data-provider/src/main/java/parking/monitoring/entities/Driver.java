package parking.monitoring.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	//@ManyToOne
	@JoinColumn(name = "car_number")
	Car car;
	long driverId;
	String name;
	String email;
	String birthdate;

	public Driver(Car car, long driverId, String name, String email, String birthdate) {
		this.car = car;
		this.driverId = driverId;
		this.name = name;
		this.email = email;
		this.birthdate = birthdate;
	}
	
	public Driver() {
		
	}

	public long getId() {
		return id;
	}

	public Car getCar() {
		return car;
	}

	public long getDriverId() {
		return driverId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getBirthdate() {
		return birthdate;
	}


	
}
