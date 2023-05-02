package parking.monitoring.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class LastScan {
	
	@Id
	long carNumber;
	LocalDateTime expiry;
	
	public LastScan(long carNumber, LocalDateTime expiry) {
		this.carNumber = carNumber;
		this.expiry = expiry;
	}
	
	public LastScan() {
		
	}
	
	public long getCarNumber() {
		return carNumber;
	}
	
	public LocalDateTime getExpiry() {
		return expiry;
	}
	
	public void setExpiry(LocalDateTime expiry) {
		this.expiry = expiry;
	}

}
