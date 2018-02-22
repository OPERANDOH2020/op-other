package eu.operando.moduleclients;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import static org.mockito.Mockito.eq;

import eu.operando.OperandoAuthenticationException;

import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ClientDataAccessNodeTests extends ClientOperandoModuleTests
{
	// Variables to test.
	private static final String HEADER_NAME_PSP_USER_IDENTIFIER = "psp-user-identifier";
	private static final String HEADER_NAME_SERVICE_TICKET = "service-ticket";
	private static final String SERVICE_ID_DAN = "op-pdr/dan";
	
	// Dummy variables to assist testing.
	private static final String ID_OSP_USER = "123456";
	private static final String PATH_PLUS = "path?param=value";
	private static final String ENDPOINT = "/" + PATH_PLUS;
	private static final String SERVICE_TICKET_FOR_DAN = "ST-DAN-GK";
	private MultivaluedMap<String, String> mapHeaders = new MultivaluedStringMap();
	
	
	private ClientAuthenticationApiOperandoClient mockClientAuthenticationApiOperandoClient = Mockito.mock(ClientAuthenticationApiOperandoClient.class);
	private ClientDataAccessNode client = new ClientDataAccessNode(ORIGIN_WIREMOCK, mockClientAuthenticationApiOperandoClient);

	@Parameter(value = 0)
	public String httpMethod;
	
	@Parameter(value = 1)
	public String body;
	
	@Parameters(name = "send {0} request (body = \"{1}\")")
	public static Collection<Object[]> data()
	{
		String[] httpMethods = new String[] { HttpMethod.POST, HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE };
		
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		for (String httpMethod : httpMethods)
		{
			String body = null;
			if (httpMethod.equals(HttpMethod.POST) || httpMethod.equals(HttpMethod.PUT))
			{
				body = "{\"attribute\": \"value\"}";
			}
			data.add(new Object[]{httpMethod, body});
		}
		
		return data;
	}
	
	@Before
	public void setUp()
	{
		mapHeaders.add("param1", "value1");
		mapHeaders.add("param2", "value2");		
	}
	
	@Test
	public void testSendRequest_CorrectHttpRequestGenerated() throws OperandoAuthenticationException
	{
		// Set up
		when(mockClientAuthenticationApiOperandoClient.requestServiceTicket(eq(SERVICE_ID_DAN))).thenReturn(SERVICE_TICKET_FOR_DAN);
				
		// Have to do this here because mapHeaders could change when passed to sendRequest.
		MultivaluedMap<String, String> headersShouldSend = new MultivaluedHashMap<String, String>(mapHeaders);
		headersShouldSend.add(HEADER_NAME_SERVICE_TICKET, SERVICE_TICKET_FOR_DAN);
		headersShouldSend.add(HEADER_NAME_PSP_USER_IDENTIFIER, ID_OSP_USER);
		
		// Exercise
		client.sendRequest(ID_OSP_USER, PATH_PLUS, httpMethod, mapHeaders, body);

		// Verify - slightly abusing endpoint vs. queryParameter parameters, but this seems acceptable in this case.
		verifyCorrectHttpRequest(httpMethod, ENDPOINT, headersShouldSend, new MultivaluedStringMap(), body, true);
	}
	
	@Test(expected = OperandoAuthenticationException.class)
	@SuppressWarnings("unchecked")
	public void testSendRequest_AuthenticationApiClientThrowsCommunicationException_CommunicationExceptionThrown() throws OperandoAuthenticationException
	{
		// Set up
		when(mockClientAuthenticationApiOperandoClient.requestServiceTicket(eq(SERVICE_ID_DAN))).thenThrow(OperandoAuthenticationException.class);

		// Exercise
		client.sendRequest(ID_OSP_USER, PATH_PLUS, httpMethod, mapHeaders, body);
	}
}