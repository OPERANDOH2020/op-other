package eu.operando.moduleclients;

import java.util.Vector;

import javax.ws.rs.HttpMethod;

import org.junit.Test;

import eu.operando.api.model.Attachment;
import eu.operando.api.model.EmailNotification;

public class ClientEmailServicesTests extends ClientOperandoModuleTests
{
	// Variables to test.
	private static final String PATH_INTERNAL_OPERANDO_INTERFACES_EMAIL_SERVICES = "/operando/interfaces/email_services";
	public static final String ENDPOINT_EMAIL_SERVICES_EMAIL_NOTIFICATION = PATH_INTERNAL_OPERANDO_INTERFACES_EMAIL_SERVICES + "/email_notification";
	
	// Dummy variables.	
	private ClientEmailServices client = new ClientEmailServices(ORIGIN_WIREMOCK);
	
	@Test
	public void testNotifyPrivacyAnalystAboutUserPrivacySettingDiscrepancy_CorrectHttpRequest()
	{
		//Set Up
		String emailAddressTo = "analyst@operando.eu";
		String content = "Message body.";
		String subject = "Message subject";
				
		//Exercise
		client.sendEmail(emailAddressTo, subject, content);

		//Verify		
		Vector<String> to = new Vector<String>();
		to.add(emailAddressTo);
		EmailNotification emailNotificationExpected = new EmailNotification(to, new Vector<String>(), new Vector<String>(), content, subject, new Vector<Attachment>());
		verifyCorrectHttpRequest(HttpMethod.POST, ENDPOINT_EMAIL_SERVICES_EMAIL_NOTIFICATION, emailNotificationExpected);
	}
}
