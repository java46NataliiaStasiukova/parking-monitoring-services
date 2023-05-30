package parking.monitoring.entities;

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

	public void setCity(String city) {
		this.city = city;
	}

	public void setAddres(String addres) {
		this.addres = addres;
	}

	@Override
	public String toString() {
		return "ParkingZone [parkingZone=" + parkingZone + ", fineCost=" + fineCost + ", city=" + city + ", addres="
				+ addres + "]";
	}


	
}
