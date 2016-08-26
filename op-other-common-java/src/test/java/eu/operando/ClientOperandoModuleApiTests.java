package eu.operando;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import eu.operando.OperandoCommunicationException.CommunicationError;

/**
 * Class which tests the code in OperandoApiModuleClient.
 */
public abstract class ClientOperandoModuleApiTests extends ClientOperandoModuleExternalTests
{
	private static final String REPORT_ID = "1234";
	private static final String FORMAT = "pdf";
	private static final String PARAMETER_ONE_NAME = "param1";
	private static final String PARAMETER_ONE_VALUE = "xxx";
	private static final String PARAMETER_TWO_NAME = "param2";
	private static final String PARAMETER_TWO_VALUE = "yyy";
	
	protected void testGetReport_NoOptionalParameters_CorrectHttpRequest(ClientOperandoModuleApi client) throws OperandoCommunicationException
	{
		testGetReport_CorrectHttpRequest(client, 0);
	}

	protected void testGetReport_OneOptionalParameter_CorrectHttpRequest(ClientOperandoModuleApi client) throws OperandoCommunicationException
	{
		testGetReport_CorrectHttpRequest(client, 1);
	}

	protected void testGetReport_TwoOptionalParameters_CorrectHttpRequest(ClientOperandoModuleApi client) throws OperandoCommunicationException
	{
		testGetReport_CorrectHttpRequest(client, 2);
	}

	protected void testGetReport_NotFoundFromReportGenerator_OperandoCommunicationExceptionThrownWithNotFoundError(ClientOperandoModuleApi client)
	{
		testGetReport_HttpStatusError_CorrectExceptionThrown(client, Status.NOT_FOUND, CommunicationError.REQUESTED_RESOURCE_NOT_FOUND);
	}

	protected void testGetReport_ServerErrorFromReportGenerator_OperandoCommunicationExceptionThrownWithErrorFromOtherModule(ClientOperandoModuleApi client)
	{
		testGetReport_HttpStatusError_CorrectExceptionThrown(client, Status.INTERNAL_SERVER_ERROR, CommunicationError.ERROR_FROM_OTHER_MODULE);
	}

	private void testGetReport_CorrectHttpRequest(ClientOperandoModuleApi client, int numberOfOptionalParameters) throws OperandoCommunicationException
	{
		// Set up
		stub(HttpMethod.GET, ENDPOINT_REPORT_GENERATOR_REPORTS, "", Status.OK);

		// Exercise
		MultivaluedMap<String, String> parametersOptional = determineParametersOptional(numberOfOptionalParameters);
		client.getReport(REPORT_ID, FORMAT, parametersOptional);

		// Verify
		HashMap<String, String> queryParametersExpected = determineQueryParametersExpected(numberOfOptionalParameters);
		verifyCorrectHttpRequestWithoutBody(HttpMethod.GET, ENDPOINT_REPORT_GENERATOR_REPORTS, queryParametersExpected);
	}

	/**
	 * Determine the properties of the hashmap representing optional parameters. 
	 * @param numberOfOptionalParameters
	 * 	the number of optional parameters to include in the request.
	 * @return
	 * 	the hashmap representing optional parameters.
	 */
	private MultivaluedMap<String, String> determineParametersOptional(int numberOfOptionalParameters)
	{
		MultivaluedMap<String, String> parametersOptional = new MultivaluedStringMap();
		if (numberOfOptionalParameters >= 1)
		{
			parametersOptional.putSingle(PARAMETER_ONE_NAME, PARAMETER_ONE_VALUE);
		}
		if (numberOfOptionalParameters >= 2)
		{
			parametersOptional.putSingle(PARAMETER_TWO_NAME, PARAMETER_TWO_VALUE);
		}
		return parametersOptional;
	}

	/**
	 * Determine the query parameters which are expected to be sent in the REST request.
	 * @param numberOfOptionalParameters
	 * 	the number of optional parameters that should be included in the request.
	 * @param reportId
	 * 	the report ID that should be requested.
	 * @param format
	 * 	the report format that should be requested.
	 * @return
	 * 	the expected query parameters.
	 */
	private HashMap<String, String> determineQueryParametersExpected(int numberOfOptionalParameters)
	{
		HashMap<String, String> queriesParamToValue = new HashMap<String, String>();
		queriesParamToValue.put("_reportID", "" + REPORT_ID);
		queriesParamToValue.put("_format", "" + FORMAT);
		if (numberOfOptionalParameters >= 1)
		{
			queriesParamToValue.put(PARAMETER_ONE_NAME, PARAMETER_ONE_VALUE);
		}
		if (numberOfOptionalParameters >= 2)
		{
			queriesParamToValue.put(PARAMETER_TWO_NAME, PARAMETER_TWO_VALUE);
		}
		return queriesParamToValue;
	}

	private void testGetReport_HttpStatusError_CorrectExceptionThrown(ClientOperandoModuleApi client, Status statusResponse,
			CommunicationError communicationErrorOnExpectedException)
	{
		// Set up
		stub(HttpMethod.GET, ENDPOINT_REPORT_GENERATOR_REPORTS, "", statusResponse);

		// Exercise
		try
		{
			client.getReport(REPORT_ID, FORMAT, new MultivaluedStringMap());
			// If we get to here, getReport hasn't thrown an exception at all, let alone the one we expect.
			fail();
		}
		catch (OperandoCommunicationException e)
		{
			// Verify
			CommunicationError error = e.getCommunitcationError();
			assertEquals(communicationErrorOnExpectedException, error);
		}
	}
}