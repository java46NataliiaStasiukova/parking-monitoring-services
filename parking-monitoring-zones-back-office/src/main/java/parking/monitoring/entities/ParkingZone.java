package parking.monitoring.entities;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import parking.monitoring.dto.ParkingZoneDto;

@Document(collection="zones")
public class ParkingZone {
	
	@Id
	String parkingZone;
	double fineCost;
	String city;
	String addres;
	
	public ParkingZone(String parkingZone, double fineCost, String city, String addres) {
		this.parkingZone = parkingZone;
		this.fineCost = fineCost;
		this.city = city;
		this.addres = addres;
	}

	public ParkingZone() {
		
	}
	
	public static ParkingZone of(ParkingZoneDto parkingZoneDto) {
		return new ParkingZone(parkingZoneDto.parkingZone,
				parkingZoneDto.fineCost, parkingZoneDto.city,
						parkingZoneDto.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(addres, city, fineCost, parkingZone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParkingZone other = (ParkingZone) obj;
		return Objects.equals(addres, other.addres) && Objects.equals(city, other.city)
				&& Double.doubleToLongBits(fineCost) == Double.doubleToLongBits(other.fineCost)
				&& Objects.equals(parkingZone, other.parkingZone);
	}

	public double getFineCost() {
		return fineCost;
	}

	public void setFineCost(double fineCost) {
		this.fineCost = fineCost;
	}

	public String getParkingZone() {
		return parkingZone;
	}

	public String getCity() {
		return city;
	}

	public String getAddres() {
		return addres;
	}


	
}
