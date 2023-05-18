package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import parking.monitoring.service.PangoDataProviderService;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class PangoDataProviderControllerTest {
	
	@MockBean
	PangoDataProviderService service;
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	
	private static final long car1 = 111;
	private static final String parkingZone1 = "1";
	private static final long car2 = 222;
	private static final String parkingZone2 = "2";
	private static final LocalDateTime from1 = LocalDateTime.now().minusMinutes(30);
	private static final LocalDateTime to1 = LocalDateTime.now().plusMinutes(30);
	
	CarPaymentData paymentData1 = new CarPaymentData(car1, parkingZone1, "paid", from1, to1);
	CarPaymentData paymentData2 = new CarPaymentData(car1, parkingZone1, "paid", from1, to1);
	
	//	public long carNumber;
//	public String parkingZone;
//	public String status;
//	public LocalDateTime paidFrom;
//	public LocalDateTime paidTo;
	

	@BeforeEach
	void mockingService() {
		
	}
	
	@Test
	void test() {
		
	}

}
