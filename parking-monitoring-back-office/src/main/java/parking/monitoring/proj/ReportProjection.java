package parking.monitoring.proj;


public interface ReportProjection extends CarProjection{

	Long getId();
	String getZone();
	String getDate();
	Double getCost();
	String getStatus();
	String getName();
	
}
