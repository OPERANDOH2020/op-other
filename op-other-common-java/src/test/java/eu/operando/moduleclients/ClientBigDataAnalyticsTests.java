package eu.operando.moduleclients;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Test;

import eu.operando.OperandoCommunicationException;
import eu.operando.api.model.AnalyticsReport;

public class ClientBigDataAnalyticsTests extends ClientOperandoModuleTests
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_BIGDATA = "/operando/core/bigdata";
	public static final String ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID = 
		PATH_INTERNAL_OPERANDO_CORE_BIGDATA + "/jobs/{job_id}/reports/latest";
	
	private ClientBigDataAnalytics client = new ClientBigDataAnalytics(ORIGIN_WIREMOCK);
	
	@Test
	public void testGetBdaReport_CorrectHttpRequest() throws OperandoCommunicationException
	{
		// Set up
		String jobId = "C456";
		String userId = "D000";
		MultivaluedStringMap headerParametersExpected = new MultivaluedStringMap();
		headerParametersExpected.add("psp-user", userId);
		String endpoint = ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID.replace("{job_id}", jobId);
		String httpMethod = HttpMethod.GET;
		stub(httpMethod, endpoint);

		// Exercise
		client.getBdaReport(jobId, userId);

		// Verify
		verifyCorrectHttpRequest(httpMethod, endpoint, headerParametersExpected, new MultivaluedStringMap(), null, false);
	}
	
	@Test(expected = OperandoCommunicationException.class)
	public void testGetBdaReport_FailedGet_HttpExceptionThrown() throws OperandoCommunicationException
	{
		// Set up
		String jobId = "C456";
		String userId = "D000";
		String endpoint = ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID.replace("{job_id}", jobId);
		String httpMethod = HttpMethod.GET;
		stub(httpMethod, endpoint, "", Status.INTERNAL_SERVER_ERROR);
		
		// Exercise
		client.getBdaReport(jobId, userId);
	}
	
	@Test
	public void testGetBdaReport_SuccessfulGet_ResponseBodyInterpretedCorrectly() throws OperandoCommunicationException
	{
		// Set up
		String jobId = "C456";
		String userId = "D000";
		AnalyticsReport reportToReturn = new AnalyticsReport("2", "Report", "a report", "CgoKCgoKCgoKCgo8IURPQ1RZUEUg");
		String endpoint = ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID.replace("{job_id}", jobId);
		String httpMethod = HttpMethod.GET;
		stub(httpMethod, endpoint, reportToReturn);

		// Exercise
		AnalyticsReport reportReturned = client.getBdaReport(jobId, userId);

		// Verify
		assertEquals(reportToReturn, reportReturned);
		
	}
}
