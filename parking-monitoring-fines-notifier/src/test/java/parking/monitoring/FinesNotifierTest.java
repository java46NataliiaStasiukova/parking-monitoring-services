package parking.monitoring;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import parking.monitoring.service.NotificationDataProvider;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class FinesNotifierTest {

	static Logger LOG = LoggerFactory.getLogger(FinesNotifierTest.class);
	
	@Autowired
	InputDestination producer;
	@MockBean
	NotificationDataProvider dataProvider;
	@RegisterExtension
	static GreenMailExtension mailExtension =
	new GreenMailExtension(ServerSetupTest.SMTP)
		.withConfiguration(GreenMailConfiguration.aConfig().withUser("fines", "12345.com"));
	
	private static final long CAR_NUMBER = 111;
	private static final long DRIVER_ID = 123;
	private static final String DRIVER_NAME = "Piter";
	private static final String DRIVER_EMAIL = "piter@gmail.com";
	private static final String PARKING_ZONE = "1";
	
	private static final long CAR_NUMBER_NO_DATA = 222;
	private static final String SERVICE_EMAIL = "reports-service@gmail.com";
	private static final String NO_NAME = "unknown driver";
	
	ParkingReport parkingReport = new ParkingReport(CAR_NUMBER, PARKING_ZONE);
	NotificationData notificationData = new NotificationData(CAR_NUMBER, DRIVER_EMAIL, DRIVER_NAME, DRIVER_ID);
	
	@Test
	void driverDataExistTest() throws MessagingException {
		LOG.debug("***TEST notificationData: {}", notificationData.toString());
		when(dataProvider.getNotificationData(CAR_NUMBER)).thenReturn(notificationData);
		producer.send(new GenericMessage<ParkingReport>(parkingReport), "finalReportsConsumer-in-0");
		MimeMessage message = mailExtension.getReceivedMessages()[0];
		assertEquals(DRIVER_EMAIL, message.getAllRecipients()[0].toString());
		assertTrue(message.getSubject().contains(DRIVER_NAME));
	}
	
	@Test
	void driverDataNotExistTest() throws MessagingException {
		LOG.debug("***TEST notificationData: NULL");
		when(dataProvider.getNotificationData(CAR_NUMBER_NO_DATA)).thenReturn(null);
		producer.send(new GenericMessage<ParkingReport>(parkingReport), "finalReportsConsumer-in-0");
		MimeMessage message = mailExtension.getReceivedMessages()[0];
		assertEquals(SERVICE_EMAIL, message.getAllRecipients()[0].toString());
		assertTrue(message.getSubject().contains(NO_NAME));
	}
	
}
