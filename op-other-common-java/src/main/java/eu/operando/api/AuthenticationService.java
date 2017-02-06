package eu.operando.api;

import eu.operando.OperandoCommunicationException;

public interface AuthenticationService {
	boolean isAuthenticatedForService(String serviceTicket, String serviceId) throws OperandoCommunicationException;
}
