package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import parking.monitoring.dto.ParkingZoneDto;
import parking.monitoring.entities.ParkingZone;
import parking.monitoring.service.ParkingZonesService;


@SpringBootTest
@AutoConfigureMockMvc
class ZonesBackOfficeControllerTest {

	static Logger LOG = LoggerFactory.getLogger(ZonesBackOfficeControllerTest.class);
	@Autowired
	MockMvc mockMvc;
	@MockBean
	ParkingZonesService service;
	ObjectMapper mapper = new ObjectMapper();
	
	ParkingZoneDto parkingDto1 = new ParkingZoneDto("1", 200.0, "Bat Yam", "Yerushalaim");
	ParkingZone parkingZone1 = new ParkingZone("1", 200.0, "Bat Yam", "Yerushalaim");
	
	ParkingZoneDto parkingDto2 = new ParkingZoneDto("1", 250.0, "Bat Yam", "Yerushalaim");
	ParkingZone parkingZone2 = new ParkingZone("1", 250.0, "Bat Yam", "Yerushalaim");
	
	@BeforeEach
	void serviceMocking() {
		when(service.addParkingZone(parkingDto1)).thenReturn(parkingZone1);
		when(service.updateParkingZone(parkingDto2)).thenReturn(parkingZone2);
		when(service.getParkingZone("1")).thenReturn(parkingZone2);
		when(service.deleteParkingZone("1")).thenReturn(parkingZone2);
		
	}
	
	@Test
	void addParkingZoneTest() throws Exception {
		LOG.debug("***TEST add new parking zone");
		String zoneJson = mapper.writeValueAsString(parkingDto1);
		String res = mockMvc.perform(post("/zones")
				.contentType(MediaType.APPLICATION_JSON)
				.content(zoneJson))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertTrue(res.contains("new parking zone"));
	}
	
	@Test
	void updateParkingZoneTest() throws Exception {
		LOG.debug("***TEST update parking zone");
		String zoneJson = mapper.writeValueAsString(parkingDto2);
		String res = mockMvc.perform(put("/zones")
				.contentType(MediaType.APPLICATION_JSON)
				.content(zoneJson))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertTrue(res.contains("was updated"));
	}
	
	@Test
	void getParkingZoneTest() throws Exception {
		LOG.debug("***TEST get parking zone");
		String res = mockMvc.perform(get("/zones/1")).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertTrue(res.contains("found parking zone"));
	}
	
	@Test
	void deleteParkingZoneTest() throws Exception {
		LOG.debug("***TEST delete parking zone");
		String res = mockMvc.perform(delete("/zones/1")).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertTrue(res.contains("was deleted"));
	}

}
