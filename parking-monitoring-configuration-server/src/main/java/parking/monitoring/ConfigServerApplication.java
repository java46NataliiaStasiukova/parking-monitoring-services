package parking.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import jakarta.annotation.PreDestroy;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);

	}
	
	@PreDestroy
	void preDestroy() {
		System.out.println("ConfiServerAppl - shutdown has been performed");
	}

}
