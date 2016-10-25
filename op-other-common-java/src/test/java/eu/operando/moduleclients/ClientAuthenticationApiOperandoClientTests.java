package eu.operando.moduleclients;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import eu.operando.OperandoAuthenticationException;

public class ClientAuthenticationApiOperandoClientTests
{
	// Dummy variables to assist testing.
	private static final String TICKET_GRANTING_TICKET = "TGT";
	private static final String TICKET_GRANTING_TICKET_2 = "TGT2";
	private static final String SERVICE_TICKET = "serviceTicket";
	private static final String SERVICE_ID = "serviceId";

	// Mocked dependencies.
	private RequestBuilderAuthenticationApi mockRequestBuilder = Mockito.mock(RequestBuilderAuthenticationApi.class);
	
	// System under test
	private ClientAuthenticationApiOperandoClient client = new ClientAuthenticationApiOperandoClient(mockRequestBuilder);

	
	@Test
	public void testRequestServiceTicket_FirstCall_TicketGrantingTicketRequested() throws OperandoAuthenticationException
	{
		// Exercise
		client.requestServiceTicket(SERVICE_ID);

		// Verify
		Mockito.verify(mockRequestBuilder).requestTicketGrantingTicket();
	}
	
	@Test
	public void testRequestServiceTicket_TicketGrantingTicketUsedWhenRequestingServiceTicket() throws OperandoAuthenticationException
	{
		// Set up
		when(mockRequestBuilder.requestTicketGrantingTicket()).thenReturn(TICKET_GRANTING_TICKET);
		
		// Exercise
		client.requestServiceTicket(SERVICE_ID);
		
		// Verify
		Mockito.verify(mockRequestBuilder).requestServiceTicket(SERVICE_ID, TICKET_GRANTING_TICKET);
	}

	@Test
	public void testRequestServiceTicket_TicketGrantingTicketValid_TicketGrantingTicketReused() throws OperandoAuthenticationException
	{
		// Set up
		when(mockRequestBuilder.requestTicketGrantingTicket()).thenReturn(TICKET_GRANTING_TICKET);

		// Exercise - call twice
		client.requestServiceTicket(SERVICE_ID);
		client.requestServiceTicket(SERVICE_ID);

		// Verify - ticket granting ticket requested once, used twice.
		Mockito.verify(mockRequestBuilder, times(1)).requestTicketGrantingTicket();
		Mockito.verify(mockRequestBuilder, times(2)).requestServiceTicket(SERVICE_ID, TICKET_GRANTING_TICKET);
	}

	@Test
	public void testRequestServiceTicket_TicketGrantingTicketValid_ServiceTicketFromAuthenticationApiReturned() throws OperandoAuthenticationException
	{
		// Set up
		when(mockRequestBuilder.requestTicketGrantingTicket()).thenReturn(TICKET_GRANTING_TICKET);
		when(mockRequestBuilder.requestServiceTicket(SERVICE_ID, TICKET_GRANTING_TICKET)).thenReturn(SERVICE_TICKET);

		// Exercise
		String serviceTicket = client.requestServiceTicket(SERVICE_ID);

		// Verify
		assertEquals(SERVICE_TICKET, serviceTicket);
	}

	@Test
	public void testRequestServiceTicket_TicketGrantingTicketInvalid_NewTicketGrantingTicketRequested() throws OperandoAuthenticationException
	{
		// Set up
		when(mockRequestBuilder.requestTicketGrantingTicket()).thenReturn(TICKET_GRANTING_TICKET, TICKET_GRANTING_TICKET_2);
		when(mockRequestBuilder.requestServiceTicket(SERVICE_ID, TICKET_GRANTING_TICKET)).thenThrow(OperandoAuthenticationException.class);

		// Exercise
		client.requestServiceTicket(SERVICE_ID);

		// Verify
		Mockito.verify(mockRequestBuilder, times(2)).requestTicketGrantingTicket(); // Called twice - once to get immediately invalidated TGT, and again because first was invalid
		Mockito.verify(mockRequestBuilder).requestServiceTicket(SERVICE_ID, TICKET_GRANTING_TICKET); // Called once - first try
		Mockito.verify(mockRequestBuilder).requestServiceTicket(SERVICE_ID, TICKET_GRANTING_TICKET_2); // Called once - because first TGT was invalid.
		
	}

	@Test(expected = OperandoAuthenticationException.class)
	public void testRequestServiceTicket_InvalidCredentials_OperandoAuthenticationExceptionThrown() throws OperandoAuthenticationException
	{
		// Set up
		when(mockRequestBuilder.requestTicketGrantingTicket()).thenThrow(OperandoAuthenticationException.class);

		// Exercise
		client.requestServiceTicket(SERVICE_ID);

		// Verify - done in test annotation
	}
}