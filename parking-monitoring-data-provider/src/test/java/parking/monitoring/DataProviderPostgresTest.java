package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
class DataProviderPostgresTest {
	
	static Logger LOG = LoggerFactory.getLogger(DataProviderPostgresTest.class);
	@Autowired
	MockMvc mockMvc;
	
	private static final long CAR_NUMBER_1 = 111;
	private static final long CAR_NUMBER_2 = 222;
	
	@Test
	@Sql(scripts = {"DriversCars.sql"})
	void dataProviderTest() throws Exception {
		String jsonResponse = mockMvc.perform(get("/driver-data/" + CAR_NUMBER_1))
				.andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		NotificationData notificationData = mapper.readValue(jsonResponse, NotificationData.class);
		LOG.debug("***Test recived notification data for car:{}, {}", CAR_NUMBER_1, notificationData.toString());
		assertEquals(112, notificationData.driverId);
		assertEquals(CAR_NUMBER_1, notificationData.carNumber);
		assertEquals("piter@gmail.com", notificationData.email);
		assertEquals("Piter", notificationData.name);
	}
	
	void dataProviderTest2() throws Exception {
		String jsonResponse = mockMvc.perform(get("/driver-data/" + CAR_NUMBER_1))
				.andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		NotificationData notificationData = mapper.readValue(jsonResponse, NotificationData.class);
		LOG.debug("***Test recived notification data for car:{}, {}", CAR_NUMBER_2, notificationData.toString());
		assertEquals(221, notificationData.driverId);
		assertEquals(CAR_NUMBER_2, notificationData.carNumber);
		assertEquals("alice@gmail.com", notificationData.email);
		assertEquals("Alice", notificationData.name);
	}

	


}
