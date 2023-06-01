package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import parking.monitoring.service.PangoDataProviderService;


@SpringBootTest
@AutoConfigureMockMvc
class PangoDataProviderControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	@MockBean
	PangoDataProviderService service;
	
	private static final long car1 = 111;
	private static final String parkingZone1 = "1";
	private static final long car2 = 222;
	private static final String parkingZone2 = "2";
	private static final LocalDateTime from1 = LocalDateTime.now().minusMinutes(30);
	private static final LocalDateTime to1 = LocalDateTime.now().plusMinutes(30);
	
	CarPaymentData paymentData1 = new CarPaymentData(car1, parkingZone1, "paid", from1, to1);
	CarPaymentData paymentData2 = new CarPaymentData(car2, parkingZone2, "not-paid", from1, to1);

	@BeforeEach
	void mockingService() {
		when(service.getCarPaymentData(car1, parkingZone1))
		.thenReturn(paymentData1);
		when(service.getCarPaymentData(car2, parkingZone2))
		.thenReturn(paymentData2);
	}
	
	@Test
	void test() throws UnsupportedEncodingException, Exception {
		String res = mockMvc.perform(get("/paymentData/111/1")).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertTrue(res.contains("paid"));
		
		String res2 = mockMvc.perform(get("/paymentData/222/2")).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertTrue(res2.contains("not-paid"));
	}

}
