package eu.operando.api;

public interface AuthenticationService {
	boolean isAuthenticatedForService(String serviceTicket, String serviceId);
}
