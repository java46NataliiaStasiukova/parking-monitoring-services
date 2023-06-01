package parking.monitoring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PreDestroy;

@SpringBootApplication
public class PangoDataProviderAppl {

	public static void main(String[] args) {
		SpringApplication.run(PangoDataProviderAppl.class, args);

	}
	
	@PreDestroy
	void preDestroy() {
		System.out.println("PangoDataProviderAppl - shutdown has been performed");
	}
	
	

}
