package parking.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PreDestroy;


@SpringBootApplication
@EnableScheduling
public class GarbageCollectorAppl {

	public static void main(String[] args) {
		SpringApplication.run(GarbageCollectorAppl.class, args);
		
	}
	
	@PreDestroy
	void preDestroy() {
		System.out.println("GarbageCollectorAppl - shutdown has been performed");
	}
	


}
