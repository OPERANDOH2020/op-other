package eu.operando.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import eu.operando.AuthenticationWrapper;
import eu.operando.OperandoCommunicationException;
import eu.operando.OperandoCommunicationException.CommunicationError;
import eu.operando.api.impl.AuthenticationServiceImpl;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoService;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTests {

	@Mock
	private ClientAuthenticationApiOperandoService delegate;
	
	@InjectMocks
	private AuthenticationServiceImpl implementation;
	
	@Test
	public void testIsAuthenticated_CorrectParameters() throws OperandoCommunicationException{
		when(delegate.isOspAuthenticatedForRequestedService(anyString(), anyString())).thenReturn(true);
		
		String serviceTicket = "A123";
		String serviceId = "B987";
		
		implementation.isAuthenticatedForService(serviceTicket, serviceId);
		
		verify(delegate).isOspAuthenticatedForRequestedService(serviceTicket, serviceId);
	}
	
	@Test
	public void testIsAuthenticated_ReturnTrueIfAuthenticated() throws OperandoCommunicationException{
		when(delegate.isOspAuthenticatedForRequestedService(anyString(), anyString())).thenReturn(true);
		
		boolean isAuthenticated = implementation.isAuthenticatedForService("A123", "B987");
		
		assertTrue(isAuthenticated);
		
	}
	
	@Test
	public void testIsAuthenticated_ReturnFalseIfNotAuthenticated() throws OperandoCommunicationException{
		when(delegate.isOspAuthenticatedForRequestedService(anyString(), anyString())).thenReturn(false);
		
		boolean isAuthenticated = implementation.isAuthenticatedForService("A123", "B987");
		
		assertFalse(isAuthenticated);
	}
	
	@Test
	public void testIsAuthenticated_ThrowInternalErrorIfCantAuthenticate() throws OperandoCommunicationException{
		CommunicationError errorToThrow = CommunicationError.ERROR_FROM_OTHER_MODULE;
		when(delegate.isOspAuthenticatedForRequestedService(anyString(), anyString()))
			.thenThrow(new OperandoCommunicationException(errorToThrow));
		try{
			implementation.isAuthenticatedForService("A123", "B987");
			fail("The authentication service should throw an error if it cannot authenticate.");
		}
		catch (OperandoCommunicationException ex){
			CommunicationError thrownError = ex.getCommunitcationError();
			assertEquals(errorToThrow, thrownError);
		}
	}
	
	@Test
	public void testRequestDetails_CorrectParameters() throws OperandoCommunicationException{
		setUpRequestDetials(true, null);
		
		String serviceTicket = "A123";
		String serviceId = "B987";
		
		implementation.requestAuthenticationDetails(serviceTicket, serviceId);
		
		verify(delegate).requestAuthenticationDetails(serviceTicket, serviceId);
	}
	
	@Test
	public void testRequestDetails_ReturnTrueIfAuthenticated() throws OperandoCommunicationException{
		setUpRequestDetials(true, null);
		
		AuthenticationWrapper wrapper = implementation.requestAuthenticationDetails("A123", "B987");
		
		assertTrue(wrapper.isTicketValid());
		
	}
	
	@Test
	public void testRequestDetails_ReturnFalseIfNotAuthenticated() throws OperandoCommunicationException{
		setUpRequestDetials(false, null);
		
		AuthenticationWrapper wrapper = implementation.requestAuthenticationDetails("A123", "B987");
		
		assertFalse(wrapper.isTicketValid());
	}
	
	@Test
	public void testRequestDetails_ReturnsCorrectUserId() throws OperandoCommunicationException{
		String userId = "D000";
		setUpRequestDetials(true, userId);
		
		AuthenticationWrapper wrapper = implementation.requestAuthenticationDetails("A123", "B987");
		
		assertEquals(userId, wrapper.getIdOspUser());
	}
	
	@Test
	public void testRequestDetails_ThrowInternalErrorIfCantAuthenticate() throws OperandoCommunicationException{
		CommunicationError errorToThrow = CommunicationError.ERROR_FROM_OTHER_MODULE;
		when(delegate.requestAuthenticationDetails(anyString(), anyString()))
			.thenThrow(new OperandoCommunicationException(errorToThrow));
		try{
			implementation.requestAuthenticationDetails("A123", "B987");
			fail("The authentication service should throw an error if it cannot authenticate.");
		}
		catch (OperandoCommunicationException ex){
			CommunicationError thrownError = ex.getCommunitcationError();
			assertEquals(errorToThrow, thrownError);
		}
	}
	
	private void setUpRequestDetials(boolean shouldAuthenticate, String userId) throws OperandoCommunicationException{
		when(delegate.requestAuthenticationDetails(anyString(), anyString()))
			.thenReturn(new AuthenticationWrapper(shouldAuthenticate, userId));
	}
}
