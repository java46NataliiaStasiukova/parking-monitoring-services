package parking.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PreDestroy;

@SpringBootApplication
public class DataProviderAppl {

	public static void main(String[] args) {
		SpringApplication.run(DataProviderAppl.class, args);

	}
	
	@PreDestroy
	void preDestroy() {
		System.out.println("DataProviderAppl - shutdown has been performed");
	}

}
