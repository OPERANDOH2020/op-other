package eu.operando.moduleclients;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

public class ClientAuthenticationServiceTests extends ClientTests
{
	// Variables independent of tests.
	private static final String BASE_PATH_AUTHENTICATION_API = "/authentication"; // TODO - waiting on UPRC to change to match.
	private static final String ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION =
			BASE_PATH_AUTHENTICATION_API + "/tickets/service_ticket/%s/validation"; // TODO - this is liable to change and should be checked.

	// Dummy variables to assist testing.
	private static final String SERVICE_TICKET = "qwerty1234";
	private static final String ENDPOINT = String.format(ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION, SERVICE_TICKET);

	private ClientAuthenticationService client = new ClientAuthenticationService(ORIGIN_WIREMOCK);

	@Test
	public void testIsOspAuthenticated_CorrectHttpRequest()
	{
		// Exercise
		client.isOspAuthenticated(SERVICE_TICKET);

		// Verify
		verifyCorrectHttpRequest(HttpMethod.GET, ENDPOINT);
	}

	@Test
	public void testIsOspAuthenticated_HandleValidTicketCorrectly()
	{
		// Set up
		stub(HttpMethod.GET, ENDPOINT, "", Status.OK);

		// Exercise
		boolean validTicket = client.isOspAuthenticated(SERVICE_TICKET);

		// Verify
		assertThat(validTicket, is(true));
	}

	@Test
	public void testIsOspAuthenticated_HandleInvalidTicketCorrectly()
	{
		// Set up
		stub(HttpMethod.GET, ENDPOINT, "", Status.BAD_REQUEST);

		// Exercise
		boolean validTicket = client.isOspAuthenticated(SERVICE_TICKET);

		// Verify
		assertThat(validTicket, is(false));
	}
	/**
	 * TODO - handle other responses e.g. 404, 500
	 */
}
