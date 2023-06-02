package parking.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import jakarta.annotation.PreDestroy;

@SpringBootApplication
@ComponentScan(basePackages = {"parking"})
public class GateWayAppl {

	public static void main(String[] args) {
		SpringApplication.run(GateWayAppl.class, args);

	}
	
	@PreDestroy
	void preDestroy() {
		System.out.println("GateWayAppl - shutdown has been performed");
	}

}
