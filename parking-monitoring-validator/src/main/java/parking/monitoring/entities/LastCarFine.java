package parking.monitoring.entities;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class LastCarFine {
	
	@Id
	long carNumber;
	String parkingZone;
	LocalDate date;
	
	public LastCarFine(long carNumber, String parkingZone, LocalDate date) {
		this.carNumber = carNumber;
		this.parkingZone = parkingZone;
		this.date = date;
	}
	
	public LastCarFine() {
		
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getParkingZone() {
		return parkingZone;
	}

	public void setParkingZone(String parkingZone) {
		this.parkingZone = parkingZone;
	}

	public long getCarNumber() {
		return carNumber;
	}


}
