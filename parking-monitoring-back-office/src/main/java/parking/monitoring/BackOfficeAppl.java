package parking.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import jakarta.annotation.PreDestroy;

@SpringBootApplication
@ComponentScan(basePackages= {"parking"})
public class BackOfficeAppl {

	public static void main(String[] args) {
		SpringApplication.run(BackOfficeAppl.class, args);

	}
	
	@PreDestroy
	void preDestroy() {
		System.out.println("BackOfficeAppl - shutdown has been performed");
	}

}
