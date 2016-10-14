package eu.operando.moduleclients;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.operando.OperandoAuthenticationException;

public class ClientAuthenticationApiOperandoClient
{
	private static final Logger LOGGER = LogManager.getLogger(ClientAuthenticationApiOperandoClient.class);
	
	private RequestBuilderAuthenticationApi requestBuilder = null;
	private String ticketGrantingTicket = "";

	public ClientAuthenticationApiOperandoClient(RequestBuilderAuthenticationApi requestBuilder)
	{
		this.requestBuilder  = requestBuilder;
	}

	public String requestServiceTicket(String serviceId) throws OperandoAuthenticationException
	{
		if (ticketGrantingTicket.isEmpty())
		{
			updateTgt();
		}
		
		String serviceTicket = "";
		
		try
		{
			serviceTicket = requestServiceTicketUsingTgt(serviceId);
		}
		catch (OperandoAuthenticationException e)
		{
			LOGGER.info("TGT invalid");
			updateTgt();
			serviceTicket = requestServiceTicketUsingTgt(serviceId);
		}
		
		return serviceTicket;
	}

	private void updateTgt() throws OperandoAuthenticationException
	{
		LOGGER.info("Requesting new TGT");
		ticketGrantingTicket = requestBuilder.requestTicketGrantingTicket();
	}

	private String requestServiceTicketUsingTgt(String serviceId) throws OperandoAuthenticationException
	{
		LOGGER.info("Requesting new ST");
		return requestBuilder.requestServiceTicket(serviceId, ticketGrantingTicket);
	}
}
