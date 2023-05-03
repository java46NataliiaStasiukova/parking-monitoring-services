package parking.monitoring;

import org.slf4j.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinesValidatorAppl {
	static Logger LOG = LoggerFactory.getLogger(FinesValidatorAppl.class);
	
	
	public static void main(String[] args) {
		SpringApplication.run(FinesValidatorAppl.class, args);

	}

}
