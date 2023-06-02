package parking.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import jakarta.annotation.PreDestroy;

@SpringBootApplication
@ComponentScan(basePackages= {"parking"})
public class ZonesBackOfficeAppl {

	public static void main(String[] args) {
		SpringApplication.run(ZonesBackOfficeAppl.class, args);

	}
	
	@PreDestroy
	void preDestroy() {
		System.out.println("ZonesBackOffice - shutdown has been performed");
	}

}
