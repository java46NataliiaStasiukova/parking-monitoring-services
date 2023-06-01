package parking.monitoring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import parking.monitoring.dto.ReportDto;
import parking.monitoring.repo.ReportRepository;
import parking.monitoring.service.FinesPopulatorService;


@SpringBootTest(properties = {"TestConstants.FILE_NAME"})
@Import(TestChannelBinderConfiguration.class)
class FinesPopulatorTest {
	
	static Logger LOG = LoggerFactory.getLogger(FinesPopulatorTest.class);
	@Autowired
	FinesPopulatorService service;
	@Autowired
	ReportRepository reportRepo;

	ReportDto reportDto1 = new ReportDto(111, 112, "1", "2023-05-20", 
			250.0, "not-paid", "Yakov");
	ReportDto reportDto2 = new ReportDto(222, 221, "1", "2023-05-20", 
			250.0, "not-paid", "Yakov");
	ReportDto reportDto3 = new ReportDto(333, 332, "1", "2023-05-20", 
			250.0, "not-paid", "Yakov");
	
	@Test
	@Sql(scripts = {"CarsDriversReports.sql"})
	void addNewReportTest() {
		service.addReport(reportDto1);
		service.addReport(reportDto2);
		assertEquals(2, reportRepo.findAll().size());
	}
	
	@Test
	void addRepotWithNoExistDriver() {
		assertThrows(IllegalStateException.class, () -> {
			service.addReport(reportDto3);
		});
	}

}
