package eu.operando.moduleclients;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ClientAuthenticationService extends ClientOperandoModule
{
	private static final String BASE_PATH_AUTHENTICATION_API = PATH_OPERANDO_INTERFACES + "/authentication";
	private static final String ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION =
			BASE_PATH_AUTHENTICATION_API + "/tickets/service_ticket/%s/validation";

	public ClientAuthenticationService(String originAuthenticationService)
	{
		super(originAuthenticationService);
	}

	/**
	 * Ask the Authentication Service to check that the caller providing this service ticket is allowed to access the requested service.
	 * 
	 * @param serviceTicket
	 *        the service ticket to be validated.
	 * @return whether the caller is allowed to access the requested service.
	 */
	public boolean isOspAuthenticated(String serviceTicket)
	{
		boolean validTicket = false;

		String endpoint = String.format(ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION, serviceTicket);
		Response response = sendRequest(HttpMethod.GET, endpoint);

		// Interpret the response.
		int status = response.getStatus();
		if (status == Status.OK.getStatusCode())
		{
			validTicket = true;
		}

		return validTicket;
	}
}
