package eu.operando.moduleclients;

import eu.operando.OperandoAuthenticationException;

public interface RequestBuilderAuthenticationApi
{
	public String requestTicketGrantingTicket() throws OperandoAuthenticationException;
	
	public String requestServiceTicket(String serviceId, String ticketGrantingTicket) throws OperandoAuthenticationException;
}
