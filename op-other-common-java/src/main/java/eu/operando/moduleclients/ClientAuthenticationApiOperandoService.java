package eu.operando.moduleclients;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import eu.operando.AuthenticationWrapper;
import eu.operando.OperandoCommunicationException;
import eu.operando.OperandoCommunicationException.CommunicationError;
import eu.operando.XmlUtils.XmlParseException;

public class ClientAuthenticationApiOperandoService extends ClientOperandoModule
{
	private static final Logger LOGGER = LogManager.getLogger(ClientAuthenticationApiOperandoService.class);
	
	// HTTP request constants
	private static final String ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_VARIABLE_SERVICE_TICKET = "/aapi/tickets/{st}/validate";
	private static final String QUERY_PARAMETER_NAME_SERVICE = "serviceId";

	public ClientAuthenticationApiOperandoService(String originAuthenticationService)
	{
		super(originAuthenticationService);
	}

	/**
	 * Ask the Authentication Service to check that the caller providing this service ticket is allowed to access the requested service.
	 * 
	 * @param serviceTicket
	 *        the service ticket to be validated.
	 * @param serviceId
	 * @return whether the caller is allowed to access the requested service.
	 * @throws OperandoCommunicationException 
	 */
	public boolean isOspAuthenticatedForRequestedService(String serviceTicket, String serviceId) throws OperandoCommunicationException
	{
		AuthenticationWrapper wrapper = requestAuthenticationDetails(serviceTicket, serviceId);
		return wrapper.isTicketValid();
	}

	/**
	 * Ask the authentication service for details of the caller's authentication.
	 * @param serviceTicket
	 * 	the ticket provided by the caller.
	 * @param serviceId
	 * 	the ID of the requested service.
	 * @return
	 * 	A wrapper representing the details of the caller's authentication for this service. Null if there is an issue (e.g. HTTP error or error with parsing). 
	 * @throws OperandoCommunicationException 
	 */
	public AuthenticationWrapper requestAuthenticationDetails(String serviceTicket, String serviceId) throws OperandoCommunicationException
	{
		Response response = sendRequestTicketValidation(serviceTicket, serviceId);
		return interpretResponseTicketValidation(response);
	}

	private AuthenticationWrapper interpretResponseTicketValidation(Response response) throws OperandoCommunicationException
	{
		AuthenticationWrapper wrapper = null;
		
		int statusCodeResponse = response.getStatus();
		String bodyResponse = response.readEntity(String.class);
		if (statusCodeResponse == Status.OK.getStatusCode())
		{
			try
			{
				wrapper = AuthenticationWrapper.fromXml(bodyResponse);
			}
			catch (XmlParseException e)
			{
				e.printStackTrace();
				throw new OperandoCommunicationException(CommunicationError.PROBLEM_INTERPRETING_RESPONSE_FROM_OTHER_MODULE);
			}
		}
		else if (statusCodeResponse == Status.BAD_REQUEST.getStatusCode())
		{
			wrapper = new AuthenticationWrapper(false, null);
		}
		else
		{
			LOGGER.error("Request to Authentication Service was not successful. Status code: " + statusCodeResponse + ", response body: '" + bodyResponse + "'.");
			throw new OperandoCommunicationException(CommunicationError.ERROR_FROM_OTHER_MODULE);
		}
		
		return wrapper;
	}

	private Response sendRequestTicketValidation(String serviceTicket, String serviceId)
	{
		MultivaluedStringMap queryParams = new MultivaluedStringMap();
		queryParams.add(QUERY_PARAMETER_NAME_SERVICE, serviceId);
		String endpoint = ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_VARIABLE_SERVICE_TICKET.replace("{st}", serviceTicket);
		Response response = sendRequestWithQueryParameters(HttpMethod.GET, endpoint, queryParams);
		return response;
	}
}
