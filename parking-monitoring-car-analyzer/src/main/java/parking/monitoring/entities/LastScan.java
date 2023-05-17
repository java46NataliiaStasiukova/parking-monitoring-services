package parking.monitoring.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class LastScan {
	
	@Id
	long carNumber;
	String parkingZone;
	LocalDateTime expiry;
	
	public LastScan(long carNumber, String parkingZone, LocalDateTime expiry) {
		this.carNumber = carNumber;
		this.parkingZone = parkingZone;
		this.expiry = expiry;
	}
	
	public LastScan() {
		
	}
	
	public long getCarNumber() {
		return carNumber;
	}
	
	public String getParkingZone() {
		return parkingZone;
	}
	
	public void setParkingZone(String parkingZone) {
		this.parkingZone = parkingZone;
	}
	
	public LocalDateTime getExpiry() {
		return expiry;
	}
	
	public void setExpiry(LocalDateTime expiry) {
		this.expiry = expiry;
	}

}
