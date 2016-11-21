package eu.operando.moduleclients;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Test;

import eu.operando.AuthenticationWrapper;
import eu.operando.OperandoCommunicationException;

public class ClientAuthenticationApiOperandoServiceTests extends ClientOperandoModuleTests
{
	// Variables to be tested.
	private static final String ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_VARIABLE_SERVICE_TICKET = "/aapi/tickets/{st}/validate";
	private static final String QUERY_PARAMETER_NAME_SERVICE = "serviceId";
	private static final String XML_RESPONSE_SUCCESS_VARIABLE_USER = "<cas:serviceResponse xmlns:cas=\"http://www.yale.edu/tp/cas\">"
			 + "\n\t<cas:authenticationSuccess>"
			 + "\n\t\t<cas:user>%s</cas:user>"
			 + "\n\t</cas:authenticationSuccess>"
			 + "\n</cas:serviceResponse>";

	// Dummy variables to assist testing.
	private static final String SERVICE_TICKET = "qwerty1234";
	private static final String ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_EXAMPLE = ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_VARIABLE_SERVICE_TICKET.replace("{st}", SERVICE_TICKET);
	private static final String SERVICE_ID = "ID";
	private static final String USERNAME = "operando";
	private static final String XML_RESPONSE_SUCCESS_EXAMPLE = String.format(XML_RESPONSE_SUCCESS_VARIABLE_USER, USERNAME);

	private ClientAuthenticationApiOperandoService client = new ClientAuthenticationApiOperandoService(ORIGIN_WIREMOCK);

	@Test
	public void testIsOspAuthenticated_CorrectHttpRequest() throws OperandoCommunicationException
	{
		// Set up
		stub(HttpMethod.GET, ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_EXAMPLE, XML_RESPONSE_SUCCESS_EXAMPLE, Status.OK);
		
		// Exercise
		client.isOspAuthenticatedForRequestedService(SERVICE_TICKET, SERVICE_ID);

		// Verify
		MultivaluedStringMap queryParamsExpected = new MultivaluedStringMap();
		queryParamsExpected.add(QUERY_PARAMETER_NAME_SERVICE, SERVICE_ID);
		verifyCorrectHttpRequestWithQueryParams(HttpMethod.GET, ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_EXAMPLE, queryParamsExpected);
	}

	@Test
	public void testIsOspAuthenticated_HandleValidTicketCorrectly() throws OperandoCommunicationException
	{		
		// Set up
		stub(HttpMethod.GET, ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_EXAMPLE, XML_RESPONSE_SUCCESS_EXAMPLE, Status.OK);

		// Exercise
		boolean validTicket = client.isOspAuthenticatedForRequestedService(SERVICE_TICKET, SERVICE_ID);

		// Verify
		assertThat(validTicket, is(true));
	}

	@Test
	public void testIsOspAuthenticated_HandleInvalidTicketCorrectly() throws OperandoCommunicationException
	{
		// Set up
		stub(HttpMethod.GET, ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_EXAMPLE, "", Status.BAD_REQUEST);

		// Exercise
		boolean validTicket = client.isOspAuthenticatedForRequestedService(SERVICE_TICKET, SERVICE_ID);

		// Verify
		assertThat(validTicket, is(false));
	}
	
	@Test
	public void testRequestAuthenticationDetails_CorrectHttpRequest() throws OperandoCommunicationException
	{
		// Set up
		stub(HttpMethod.GET, ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_EXAMPLE, XML_RESPONSE_SUCCESS_EXAMPLE, Status.OK);
		
		// Exercise
		client.requestAuthenticationDetails(SERVICE_TICKET, SERVICE_ID);
		
		// Verify
		MultivaluedStringMap queryParamsExpected = new MultivaluedStringMap();
		queryParamsExpected.add(QUERY_PARAMETER_NAME_SERVICE, SERVICE_ID);
		verifyCorrectHttpRequestWithQueryParams(HttpMethod.GET, ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_EXAMPLE, queryParamsExpected);
	}
	
	@Test
	public void testRequestAuthenticationDetails_HandleValidTicketCorrectly() throws OperandoCommunicationException
	{		
		// Set up
		stub(HttpMethod.GET, ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_EXAMPLE, XML_RESPONSE_SUCCESS_EXAMPLE, Status.OK);
		
		// Exercise
		AuthenticationWrapper wrapper = client.requestAuthenticationDetails(SERVICE_TICKET, SERVICE_ID);
		
		// Verify
		boolean validTicket = wrapper.isTicketValid();
		String idOspUser = wrapper.getIdOspUser();
		assertThat(validTicket, is(true));
		assertThat(idOspUser, is(equalTo(USERNAME)));
	}
	
	@Test
	public void testRequestAuthenticationDetails_HandleInvalidTicketCorrectly() throws OperandoCommunicationException
	{
		// Set up
		stub(HttpMethod.GET, ENDPOINT_AUTHENTICATION_API_VALIDATE_TICKET_EXAMPLE, "", Status.BAD_REQUEST);
		
		// Exercise
		AuthenticationWrapper wrapper = client.requestAuthenticationDetails(SERVICE_TICKET, SERVICE_ID);
		
		// Verify
		boolean validTicket = wrapper.isTicketValid();
		assertThat(validTicket, is(false));
	}
}
