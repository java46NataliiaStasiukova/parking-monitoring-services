package parking.monitoring.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "reports")
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@ManyToOne
	@JoinColumn(name = "car_number")
	Car car;
	long driver;
	String zone;
	String date;
	double cost;
	String status;
	String name;

	public Report(Car car, long driver, String zone, String date, double cost, String status, String name) {
		this.car = car;
		this.driver = driver;
		this.zone = zone;
		this.date = date;
		this.cost = cost;
		this.status = status;
		this.name = name;
	}

	public Report() {

	}

	public long getId() {
		return id;
	}

	public Car getCar() {
		return car;
	}
	
	public long getDriver() {
		return driver;
	}

	public String getZone() {
		return zone;
	}

	public String getDate() {
		return date;
	}

	public double getCost() {
		return cost;
	}

	public String getStatus() {
		return status;
	}

	public String getName() {
		return name;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
