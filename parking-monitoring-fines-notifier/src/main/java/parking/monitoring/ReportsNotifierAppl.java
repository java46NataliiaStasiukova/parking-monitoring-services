package parking.monitoring;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import parking.monitoring.dto.ReportDto;
import parking.monitoring.service.NotificationDataProvider;

@SpringBootApplication
public class ReportsNotifierAppl {

	static Logger LOG = LoggerFactory.getLogger(ReportsNotifierAppl.class);
	@Autowired
	JavaMailSender mailSender;
	@Autowired
	NotificationDataProvider dataProvider;
	@Value("${app.mail.subject: New Parking fine}")
	String subject;
	@Value("${app.mail.address.reports.service:reports-service@gmail.com}")
	String reportsServiceEmail;
	
	@Autowired
	StreamBridge streamBridge;
	@Value("{app.final.reports.binding.name:fine-out-0}")
	private String bindingName;
	
	public static void main(String[] args) {
		SpringApplication.run(ReportsNotifierAppl.class, args);

	}
	
	@Bean
	Consumer<ParkingReport> finalReportsConsumer(){
		return this::reportProcessing;
	}
	
	void reportProcessing(ParkingReport report) {
		LOG.debug("*notifier* received new fine for car with number: {}", report.carNumber);
		ReportDto reportDto = sendMail(report);
		if(reportDto != null) {
			streamBridge.send(bindingName, reportDto);
		}
	}

	private ReportDto sendMail(ParkingReport report) {
		ReportDto res = null;
		NotificationData data = dataProvider.getNotificationData(report.carNumber);
		if(data == null) {
			LOG.warn("*notifier* no email found for driver");
			data.email = reportsServiceEmail;
			data.name = "driver name was not found";
			//TODO
		} else {
			//TODO: fine cost
			res = new ReportDto(report.carNumber, data.driverId,
					report.parkingZone, LocalDateTime.now().toString(), 250.0, "not-paid", data.name);
		}
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setTo(data.email);
		smm.setSubject(subject + " " + data.name);
		String text = getMailText(data, res, report);
		smm.setText(text);
		mailSender.send(smm);
		LOG.trace("sent text mail {}",text);
		return res;
	}

	private String getMailText(NotificationData data, ReportDto dto, ParkingReport report) {
		String res = String.format("Driver's data was not found for car with number: %s, "
				+ "that parked on parking zone: %s, date: %s", report.carNumber, 
				report.parkingZone, LocalDateTime.now().toString());
		if(data != null && dto != null) {
			res = String.format("Dear %s\n You have recived new parking fine:\n%s",
					data.name, dto.toString());
		}
		return res;
	}
}
