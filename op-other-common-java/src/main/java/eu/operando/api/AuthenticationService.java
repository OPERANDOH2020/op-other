package eu.operando.api;

import eu.operando.OperandoCommunicationException;

public interface AuthenticationService {
	/**
	 * Ask the Authentication Service to check that the caller providing this service ticket is allowed to access the requested service.
	 * 
	 * @param serviceTicket
	 *        the service ticket to be validated.
	 * @param serviceId
	 * @return whether the caller is allowed to access the requested service.
	 * @throws OperandoCommunicationException 
	 */
	boolean isAuthenticatedForService(String serviceTicket, String serviceId) throws OperandoCommunicationException;
}
