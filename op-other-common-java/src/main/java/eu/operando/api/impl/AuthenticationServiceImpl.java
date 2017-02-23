package eu.operando.api.impl;

import eu.operando.AuthenticationWrapper;
import eu.operando.OperandoCommunicationException;
import eu.operando.UnableToGetDataException;
import eu.operando.api.AuthenticationService;
import eu.operando.api.factories.AuthenticationServiceFactory;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoService;

public class AuthenticationServiceImpl extends AuthenticationServiceFactory implements AuthenticationService
{

	private ClientAuthenticationApiOperandoService clientAuthenticationService = null;

	public AuthenticationServiceImpl(ClientAuthenticationApiOperandoService clientAuthenticationService)
	{
		this.clientAuthenticationService = clientAuthenticationService;
	}

	@Override
	public boolean isAuthenticatedForService(String serviceTicket, String serviceId) throws UnableToGetDataException
	{
		try
		{
			AuthenticationWrapper authenticationDetails = clientAuthenticationService.requestAuthenticationDetails(serviceTicket, serviceId);
			return authenticationDetails.isTicketValid();
		}
		catch (OperandoCommunicationException ex)
		{
			ex.printStackTrace();
			throw new UnableToGetDataException(ex);
		}
	}

	@Override
	public AuthenticationWrapper requestAuthenticationDetails(String serviceTicket, String serviceId) throws UnableToGetDataException
	{
		try
		{
			return clientAuthenticationService.requestAuthenticationDetails(serviceTicket, serviceId);
		}
		catch (OperandoCommunicationException ex)
		{
			ex.printStackTrace();
			throw new UnableToGetDataException(ex);
		}
	}

}
