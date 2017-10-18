package eu.operando.moduleclients;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import eu.operando.OperandoAuthenticationException;

public class ClientDataAccessNode extends ClientOperandoModule
{
	// Service ID for DAN.
	private static final String SERVICE_ID_DATA_ACCESS_NODE = "op-pdr/dan";
	
	// Headers
	private static final String HEADER_NAME_PSP_USER_IDENTIFIER = "psp-user-identifier";
	private static final String HEADER_NAME_SERVICE_TICKET = "service-ticket";

	private ClientAuthenticationApiOperandoClient clientAuthenticationApi = null;

	public ClientDataAccessNode(String originDataAccessNode, ClientAuthenticationApiOperandoClient clientAuthenticationService)
	{
		super(originDataAccessNode, true);
		this.clientAuthenticationApi  = clientAuthenticationService;
	}

	public Response sendRequest(String idOspUser, String pathPlus, String httpMethod, MultivaluedMap<String, String> headersToDan, String body) throws OperandoAuthenticationException
	{
		String serviceTicket = requestServiceTicket(SERVICE_ID_DATA_ACCESS_NODE);
		
		headersToDan.add(HEADER_NAME_SERVICE_TICKET, serviceTicket);
		headersToDan.add(HEADER_NAME_PSP_USER_IDENTIFIER, idOspUser);
		
		String endpoint = "/" + pathPlus;
		return sendRequest(httpMethod, endpoint, headersToDan, body, new MultivaluedStringMap());
	}

	private String requestServiceTicket(String serviceId) throws OperandoAuthenticationException
	{
		return clientAuthenticationApi.requestServiceTicket(serviceId);
	}
}
