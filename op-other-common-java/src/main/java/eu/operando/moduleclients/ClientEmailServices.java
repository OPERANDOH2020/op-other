package eu.operando.moduleclients;

import java.util.Vector;

import javax.ws.rs.HttpMethod;

import eu.operando.api.model.Attachment;
import eu.operando.api.model.EmailNotification;

public class ClientEmailServices extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_INTERFACES_EMAIL_SERVICES = "/operando/interfaces/email_services";
	private static final String ENDPOINT_EMAIL_SERVICES_EMAIL_NOTIFICATION = PATH_INTERNAL_OPERANDO_INTERFACES_EMAIL_SERVICES + "/email_notification";
	
	public ClientEmailServices(String originEmailServices)
	{
		super(originEmailServices);
	}

	public void sendEmail(String emailAddressTo, String subject, String content)
	{
		Vector<String> to = new Vector<String>();
		to.addElement(emailAddressTo);
		EmailNotification emailNotification = new EmailNotification(to, new Vector<String>(), new Vector<String>(), content, subject, new Vector<Attachment>());

		sendRequest(HttpMethod.POST, ENDPOINT_EMAIL_SERVICES_EMAIL_NOTIFICATION, emailNotification);
	}
}
