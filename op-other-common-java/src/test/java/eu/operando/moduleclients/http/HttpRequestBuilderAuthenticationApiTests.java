package eu.operando.moduleclients.http;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import eu.operando.CredentialsOperando;
import eu.operando.OperandoAuthenticationException;
import eu.operando.moduleclients.ClientOperandoModuleTests;

public class HttpRequestBuilderAuthenticationApiTests extends ClientOperandoModuleTests
{
	// Variables to test.
	private static final String ENDPOINT_TICKET_GRANTING_TICKETS = "/aapi/tickets"; // TODO - the actual values might be different.
	private static final String ENDPOINT_SERVICE_TICKETS_VARIABLE_ID = "/aapi/tickets/{tgt}";
	
	// Dummy variables to assist testing.
	private static final String TICKET_GRANTING_TICKET = "TGT";
	private static final String SERVICE_ID = "serviceId";
	private static final String SERVICE_TICKET = "serviceTicket";
	private CredentialsOperando credentials = new CredentialsOperando("username", "password");
	private String endpointServiceTicketsExample = ENDPOINT_SERVICE_TICKETS_VARIABLE_ID.replace("{tgt}", TICKET_GRANTING_TICKET);
	
	// System under test
	HttpRequestBuilderAuthenticationApi requestBuilder = new HttpRequestBuilderAuthenticationApi(ORIGIN_WIREMOCK, credentials);
	
	@Test
	public void testRequestTicketGrantingTicket_CorrectHttpRequest() throws OperandoAuthenticationException
	{
		// Exercise
		requestBuilder.requestTicketGrantingTicket();
		
		// Verify
		verifyCorrectHttpRequest(HttpMethod.POST, ENDPOINT_TICKET_GRANTING_TICKETS, credentials);
		
	}
	
	@Test
	public void testRequestTicketGrantingTicket_CorrectValueReturned() throws OperandoAuthenticationException
	{
		// Set up
		stub(HttpMethod.POST, ENDPOINT_TICKET_GRANTING_TICKETS, TICKET_GRANTING_TICKET, Status.CREATED);
		
		// Exercise
		String ticketGrantingTicket = requestBuilder.requestTicketGrantingTicket();
		
		// Verify
		assertEquals(TICKET_GRANTING_TICKET, ticketGrantingTicket);
		
	}
	
	@Test
	public void testRequestServiceTicket_CorrectHttpRequest() throws OperandoAuthenticationException
	{
		// Exercise
		requestBuilder.requestServiceTicket(SERVICE_ID, TICKET_GRANTING_TICKET);
		
		// Verify
		verifyCorrectHttpRequest(HttpMethod.POST, endpointServiceTicketsExample, SERVICE_ID);
		
	}
	
	@Test
	public void testRequestServiceTicket_CorrectValueReturned() throws OperandoAuthenticationException
	{
		// Set up
		stub(HttpMethod.POST, endpointServiceTicketsExample, SERVICE_TICKET, Status.CREATED);
		
		// Exercise		
		String serviceTicketReturned = requestBuilder.requestServiceTicket(SERVICE_ID, TICKET_GRANTING_TICKET);
		
		// Verify
		assertEquals(SERVICE_TICKET, serviceTicketReturned);
		
	}
	
}
