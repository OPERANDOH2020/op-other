package eu.operando.moduleclients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Ignore;
import org.junit.Test;

import eu.operando.OperandoCommunicationException;
import eu.operando.OperandoCommunicationException.CommunicationError;

public class ClientReportGeneratorTests extends ClientOperandoModuleTests
{
	private static final String ENCODED_REPORT = "encoded report";
	private static final String PATH_INTERNAL_OPERANDO_WEBUI_REPORTS = "/operando/webui/reports";
	private static final String ENDPOINT_REPORT_GENERATOR_REPORTS = PATH_INTERNAL_OPERANDO_WEBUI_REPORTS + "/reports";

	// Testing variables.
	private static final String REPORT_ID = "1234";
	private static final String FORMAT = "pdf";
	private static final String PARAMETER_ONE_NAME = "param1";
	private static final String PARAMETER_ONE_VALUE = "xxx";
	private static final String PARAMETER_TWO_NAME = "param2";
	private static final String PARAMETER_TWO_VALUE = "yyy";

	private ClientReportGenerator client = new ClientReportGenerator(ORIGIN_WIREMOCK);

	@Test
	public void testGetReport_NoOptionalParameters_CorrectHttpRequest() throws OperandoCommunicationException
	{
		testGetReport_CorrectHttpRequest(0);
	}

	@Test
	public void testGetReport_OneOptionalParameter_CorrectHttpRequest() throws OperandoCommunicationException
	{
		testGetReport_CorrectHttpRequest(1);
	}

	@Test
	public void testGetReport_TwoOptionalParameters_CorrectHttpRequest() throws OperandoCommunicationException
	{
		testGetReport_CorrectHttpRequest(2);
	}

	@Test
	public void testGetReport_NotFoundFromReportGenerator_OperandoCommunicationExceptionThrownWithNotFoundError()
	{
		testGetReport_HttpStatusError_CorrectExceptionThrown(Status.NOT_FOUND, CommunicationError.REQUESTED_RESOURCE_NOT_FOUND);
	}

	@Test
	public void testGetReport_ServerErrorFromReportGenerator_OperandoCommunicationExceptionThrownWithErrorFromOtherModule()
	{
		testGetReport_HttpStatusError_CorrectExceptionThrown(Status.INTERNAL_SERVER_ERROR, CommunicationError.ERROR_FROM_OTHER_MODULE);
	}

	@Test
	public void testGetReport_OkFromReportGenerator_ReportInterpretedCorreclty() throws OperandoCommunicationException
	{
		// Set up
		stub(HttpMethod.GET, ENDPOINT_REPORT_GENERATOR_REPORTS, ENCODED_REPORT, Status.OK);

		// Exercise
		MultivaluedMap<String, String> parametersOptional = determineParametersOptional(0);
		String reportFromRg = client.getReport(REPORT_ID, FORMAT, parametersOptional);		
		
		// TODO - this is yet to be implemented; need to know how a report is represented.
		assertEquals(ENCODED_REPORT, reportFromRg);
	}

	private void testGetReport_CorrectHttpRequest(int numberOfOptionalParameters) throws OperandoCommunicationException
	{
		// Set up
		stub(HttpMethod.GET, ENDPOINT_REPORT_GENERATOR_REPORTS, "", Status.OK);

		// Exercise
		MultivaluedMap<String, String> parametersOptional = determineParametersOptional(numberOfOptionalParameters);
		client.getReport(REPORT_ID, FORMAT, parametersOptional);

		// Verify
		MultivaluedMap<String, String> queryParametersExpected = new MultivaluedStringMap(parametersOptional);
		queryParametersExpected.putSingle("_reportID", REPORT_ID);
		queryParametersExpected.putSingle("_format", FORMAT);
		verifyCorrectHttpRequestWithQueryParams(HttpMethod.GET, ENDPOINT_REPORT_GENERATOR_REPORTS, queryParametersExpected);
	}

	/**
	 * Determine the properties of the hashmap representing optional parameters.
	 * 
	 * @param numberOfOptionalParameters
	 *        the number of optional parameters to include in the request.
	 * @return the hashmap representing optional parameters.
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

	private void testGetReport_HttpStatusError_CorrectExceptionThrown(Status statusResponse, CommunicationError communicationErrorOnExpectedException)
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
