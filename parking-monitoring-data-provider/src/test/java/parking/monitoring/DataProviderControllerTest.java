package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import parking.monitoring.dto.ParkingZoneDto;
import parking.monitoring.service.DataProviderService;


@SpringBootTest
@AutoConfigureMockMvc
class DataProviderControllerTest {
	
	static Logger LOG = LoggerFactory.getLogger(DataProviderControllerTest.class);
	@Autowired
	MockMvc mockMvc;
	@MockBean
	DataProviderService service;
	
	private static final String PARKING_ZONE = "1";
	ParkingZoneDto parkingDto = new ParkingZoneDto("1", 250.0, "Bat Yam", "Yerushalaim");
	
	private static final long CAR_NUMBER = 111;
	NotificationData notificationData = new NotificationData(CAR_NUMBER, "yakov@gmail.com", "Yakov", 121);
	
	
	@BeforeEach
	void serviceMocking() {
		when(service.getParkingZoneData(PARKING_ZONE))
		.thenReturn(parkingDto);
		when(service.getNotificationData(CAR_NUMBER))
		.thenReturn(notificationData);
	}
	

	@Test
	void getParkingZoneTest() throws UnsupportedEncodingException, Exception {
		String res = mockMvc.perform(get("/driver-data/parkingZone/1"))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertTrue(res.contains("Yerushalaim"));
	}
	
	@Test 
	void getNotificationDataTest() throws UnsupportedEncodingException, Exception {
		String res = mockMvc.perform(get("/driver-data/111"))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertTrue(res.contains("yakov@gmail.com"));
	}

}
