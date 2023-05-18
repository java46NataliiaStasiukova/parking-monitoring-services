package parking.monitoring.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class LastCarPayment {
	
	@Id
	long carNumber;
	String parkingZone;
	String status;
	LocalDateTime paidTill;
	
	public LastCarPayment(long carNumber, String parkingZone, String status, LocalDateTime paidTill) {
		this.carNumber = carNumber;
		this.parkingZone = parkingZone;
		this.status = status;
		this.paidTill = paidTill;
	}
	
	public LastCarPayment() {
		
	}
	
	public String getParkingZone() {
		return this.parkingZone;
	}
	
	public void setParkingZone(String parkingZone) {
		this.parkingZone = parkingZone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getPaidTill() {
		return paidTill;
	}

	public void setPaidTill(LocalDateTime paidTill) {
		this.paidTill = paidTill;
	}

	public long getCarNumber() {
		return carNumber;
	}
	
	


}
