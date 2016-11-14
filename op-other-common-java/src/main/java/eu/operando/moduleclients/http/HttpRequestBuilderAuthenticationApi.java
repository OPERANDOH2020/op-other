package eu.operando.moduleclients.http;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;

import eu.operando.CredentialsOperando;
import eu.operando.OperandoAuthenticationException;
import eu.operando.moduleclients.ClientOperandoModule;
import eu.operando.moduleclients.RequestBuilderAuthenticationApi;

public class HttpRequestBuilderAuthenticationApi extends ClientOperandoModule
												implements RequestBuilderAuthenticationApi
{
	private static final String ENDPOINT_AUTHENTICATION_AAPI_TICKETS = "/aapi/tickets";
	private static final String ENDPOINT_SERVICE_TICKETS_VARIABLE_ID = "/aapi/tickets/{tgt}";
	
	private CredentialsOperando credentials = null;

	public HttpRequestBuilderAuthenticationApi(String originAuthenticationService, CredentialsOperando credentials)
	{
		super(originAuthenticationService);
		this.credentials  = credentials;
	}

	@Override
	public String requestTicketGrantingTicket() throws OperandoAuthenticationException
	{
		Response response = sendRequest(HttpMethod.POST, ENDPOINT_AUTHENTICATION_AAPI_TICKETS, credentials);
		String ticketGrantingTicket = response.readEntity(String.class);
		
		return ticketGrantingTicket;
	}

	@Override
	public String requestServiceTicket(String serviceId, String ticketGrantingTicket) throws OperandoAuthenticationException
	{
		String endpoint = ENDPOINT_SERVICE_TICKETS_VARIABLE_ID.replace("{tgt}", ticketGrantingTicket);
		//String serviceIdBody = "{\"serviceId\": \"" + serviceId + "\"}";
		//Response response = sendRequest(HttpMethod.POST, endpoint, serviceIdBody);
		Response response = sendRequest(HttpMethod.POST, endpoint, serviceId);
		String serviceTicket = response.readEntity(String.class);
		
		return serviceTicket;
	}

}
