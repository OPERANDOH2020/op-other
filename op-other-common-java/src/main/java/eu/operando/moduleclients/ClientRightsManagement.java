package eu.operando.moduleclients;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import eu.operando.OperandoAuthenticationException;

public class ClientRightsManagement  extends ClientOperandoModule
{
	// Service ID for RM.
	private static final String SERVICE_ID_RIGHTS_MANAGEMENT = "/operando/rm/";
	
	// Headers
	private static final String HEADER_NAME_PSP_USER_IDENTIFIER = "psp-user-identifier";
	private static final String HEADER_NAME_SERVICE_TICKET = "service-ticket";

	private ClientAuthenticationApiOperandoClient clientAuthenticationApi = null;

	public ClientRightsManagement(String originRightsManagement, ClientAuthenticationApiOperandoClient clientAuthenticationService)
	{
		super(originRightsManagement, true);
		this.clientAuthenticationApi  = clientAuthenticationService;
	}

	public Response sendRequest(String httpMethod, MultivaluedMap<String, String> headersToDan, String idOspUser, String pathPlus, MultivaluedMap<String, String> queryParameters, String body) throws OperandoAuthenticationException
	{
		String serviceTicket = requestServiceTicket(SERVICE_ID_RIGHTS_MANAGEMENT);
		
		headersToDan.add(HEADER_NAME_SERVICE_TICKET, serviceTicket);
		headersToDan.add(HEADER_NAME_PSP_USER_IDENTIFIER, idOspUser);
		
		String endpoint = "/" + pathPlus;
		return sendRequest(httpMethod, endpoint, headersToDan, body, queryParameters);
	}

	private String requestServiceTicket(String serviceId) throws OperandoAuthenticationException
	{
		return clientAuthenticationApi.requestServiceTicket(serviceId);
	}	
}

