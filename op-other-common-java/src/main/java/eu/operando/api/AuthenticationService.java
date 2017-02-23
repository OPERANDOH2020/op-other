package eu.operando.api;

import eu.operando.AuthenticationWrapper;
import eu.operando.OperandoCommunicationException;
import eu.operando.UnableToGetDataException;

public interface AuthenticationService {
	/**
	 * Ask the Authentication Service to check that the caller providing this service ticket is allowed to access the requested service.
	 * 
	 * @param serviceTicket
	 *        the service ticket to be validated.
	 * @param serviceId
	 * @return whether the caller is allowed to access the requested service.
	 * @throws UnableToGetDataException 
	 */
	boolean isAuthenticatedForService(String serviceTicket, String serviceId) throws UnableToGetDataException;
	
	/**
	 * Ask the authentication service for details of the caller's authentication.
	 * @param serviceTicket
	 * 	the ticket provided by the caller.
	 * @param serviceId
	 * 	the ID of the requested service.
	 * @return
	 * 	A wrapper representing the details of the caller's authentication for this service. Null if there is an issue (e.g. HTTP error or error with parsing). 
	 * @throws UnableToGetDataException 
	 */
	AuthenticationWrapper requestAuthenticationDetails(String serviceTicket, String serviceId) throws UnableToGetDataException;
}
