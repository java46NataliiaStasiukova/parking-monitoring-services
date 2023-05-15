package parking.monitoring.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "drivers")
public class Driver {

	@Id
	long id;
	String name;
	String email;
	String birthdate;
	
//	@OneToOne(mappedBy = "driver", cascade = CascadeType.REMOVE)
//	Car car;
	

	public Driver(long id, String name, String email, String birthdate) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.birthdate = birthdate;
	}

	public Driver() {

	}

	public long getId() {
		return id;
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

	public void setEmail(String email) {
		this.email = email;
	}

}
