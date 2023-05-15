package parking.monitoring.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class LastCarFine {
	
	@Id
	long carNumber;
	String parkingZone;
	
	public LastCarFine(long carNumber, String parkingZone) {
		this.carNumber = carNumber;
		this.parkingZone = parkingZone;
	}
	
	public LastCarFine() {
		
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
