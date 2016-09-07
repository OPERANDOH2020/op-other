package eu.operando.moduleclients;

import javax.ws.rs.HttpMethod;

import org.junit.Test;

import eu.operando.api.model.LogOperando;

public class ClientLogDbTests extends ClientTests
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_LOG_DB = "/operando/core/log_db";
	private static final String ENDPOINT_LOG_DB_LOG = PATH_INTERNAL_OPERANDO_CORE_LOG_DB + "/log";

	private ClientLogDb client = new ClientLogDb(ORIGIN_WIREMOCK);

	@Test
	public void testLogActivity_CorrectHttpRequest()
	{
		// Set up
		LogOperando logOperando = new LogOperando("requesterType", "ID", "priority", "dataType", "title", "desc");

		// Exercise
		client.logActivity(logOperando);

		// Verify
		verifyCorrectHttpRequest(HttpMethod.POST, ENDPOINT_LOG_DB_LOG, logOperando);
	}
}
