package eu.operando.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import eu.operando.AuthenticationWrapper;
import eu.operando.OperandoCommunicationException;
import eu.operando.OperandoCommunicationException.CommunicationError;
import eu.operando.UnableToGetDataException;
import eu.operando.api.impl.AuthenticationServiceImpl;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoService;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTests
{

	private ClientAuthenticationApiOperandoService mockClient = Mockito.mock(ClientAuthenticationApiOperandoService.class);

	private AuthenticationServiceImpl implementation = new AuthenticationServiceImpl(mockClient);

	@Test
	public void testIsAuthenticated_CallsClientCorrectly() throws OperandoCommunicationException, UnableToGetDataException, UnableToGetDataException
	{
		setUpRequestDetials(true, null);

		String serviceTicket = "A123";
		String serviceId = "B987";

		implementation.isAuthenticatedForService(serviceTicket, serviceId);

		verify(mockClient).requestAuthenticationDetails(serviceTicket, serviceId);
	}

	@Test
	public void testIsAuthenticated_ReturnTrueIfAuthenticated() throws OperandoCommunicationException, UnableToGetDataException
	{
		setUpRequestDetials(true, null);

		boolean isAuthenticated = implementation.isAuthenticatedForService("A123", "B987");

		assertTrue(isAuthenticated);

	}

	@Test
	public void testIsAuthenticated_ReturnFalseIfNotAuthenticated() throws OperandoCommunicationException, UnableToGetDataException
	{
		setUpRequestDetials(false, null);

		boolean isAuthenticated = implementation.isAuthenticatedForService("A123", "B987");

		assertFalse(isAuthenticated);
	}

	@Test(expected = UnableToGetDataException.class)
	public void testIsAuthenticated_ThrowInternalErrorIfCantAuthenticate() throws OperandoCommunicationException, UnableToGetDataException
	{
		OperandoCommunicationException exceptionToThrow = new OperandoCommunicationException(CommunicationError.ERROR_FROM_OTHER_MODULE);
		when(mockClient.requestAuthenticationDetails(anyString(), anyString())).thenThrow(exceptionToThrow);
		
		implementation.isAuthenticatedForService("A123", "B987");
	}

	@Test
	public void testRequestDetails_CallsClientCorrectly() throws OperandoCommunicationException, UnableToGetDataException
	{
		setUpRequestDetials(true, null);

		String serviceTicket = "A123";
		String serviceId = "B987";

		implementation.requestAuthenticationDetails(serviceTicket, serviceId);

		verify(mockClient).requestAuthenticationDetails(serviceTicket, serviceId);
	}

	@Test
	public void testRequestDetails_ReturnTrueIfAuthenticated() throws OperandoCommunicationException, UnableToGetDataException
	{
		setUpRequestDetials(true, null);

		AuthenticationWrapper wrapper = implementation.requestAuthenticationDetails("A123", "B987");

		assertTrue(wrapper.isTicketValid());

	}

	@Test
	public void testRequestDetails_ReturnFalseIfNotAuthenticated() throws OperandoCommunicationException, UnableToGetDataException
	{
		setUpRequestDetials(false, null);

		AuthenticationWrapper wrapper = implementation.requestAuthenticationDetails("A123", "B987");

		assertFalse(wrapper.isTicketValid());
	}

	@Test
	public void testRequestDetails_ReturnsCorrectUserId() throws OperandoCommunicationException, UnableToGetDataException
	{
		String userId = "D000";
		setUpRequestDetials(true, userId);

		AuthenticationWrapper wrapper = implementation.requestAuthenticationDetails("A123", "B987");

		assertEquals(userId, wrapper.getIdOspUser());
	}

	@Test(expected = UnableToGetDataException.class)
	public void testRequestDetails_ThrowInternalErrorIfCantAuthenticate() throws OperandoCommunicationException, UnableToGetDataException
	{
		CommunicationError errorToThrow = CommunicationError.ERROR_FROM_OTHER_MODULE;
		when(mockClient.requestAuthenticationDetails(anyString(), anyString())).thenThrow(new OperandoCommunicationException(errorToThrow));
		
		implementation.requestAuthenticationDetails("A123", "B987");
	}

	private void setUpRequestDetials(boolean ticketIsValid, String userId) throws OperandoCommunicationException
	{
		when(mockClient.requestAuthenticationDetails(anyString(), anyString())).thenReturn(new AuthenticationWrapper(ticketIsValid, userId));
	}
}
