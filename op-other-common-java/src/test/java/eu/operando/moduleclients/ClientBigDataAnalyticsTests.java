package eu.operando.moduleclients;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Test;
import org.mockito.Mockito;

import eu.operando.OperandoAuthenticationException;
import eu.operando.OperandoCommunicationException;
import eu.operando.api.model.AnalyticsReport;

public class ClientBigDataAnalyticsTests extends ClientOperandoModuleTests
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_BIGDATA = "/operando/core/bigdata";
	public static final String ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID = 
		PATH_INTERNAL_OPERANDO_CORE_BIGDATA + "/jobs/{job_id}/reports/latest";
	
	private static final String SERVICE_ID_BIG_DATA_ANALYTICS = "GET/osp/bda/jobs/.*/reports";
	
	private ClientAuthenticationApiOperandoClient mockClientAuthenticationApiOperandoClient = Mockito.mock(ClientAuthenticationApiOperandoClient.class);
	private ClientBigDataAnalytics client = new ClientBigDataAnalytics(ORIGIN_WIREMOCK, mockClientAuthenticationApiOperandoClient);
	
	@Test
	public void testGetBdaReport_CorrectHttpRequest() throws OperandoCommunicationException, OperandoAuthenticationException
	{
		// Set up
		String jobId = "C456";
		String serviceTicket = "abcd";
		when(mockClientAuthenticationApiOperandoClient.requestServiceTicket(SERVICE_ID_BIG_DATA_ANALYTICS)).thenReturn(serviceTicket);
		String userId = "D000";
		MultivaluedStringMap headerParametersExpected = new MultivaluedStringMap();
		headerParametersExpected.add("psp-user", userId);
		headerParametersExpected.add("service-ticket", serviceTicket);
		String endpoint = ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID.replace("{job_id}", jobId);
		String httpMethod = HttpMethod.GET;
		stub(httpMethod, endpoint);

		// Exercise
		client.getBdaReport(jobId, userId);

		// Verify
		verifyCorrectHttpRequest(httpMethod, endpoint, headerParametersExpected, new MultivaluedStringMap(), null, false);
	}
	
	@Test(expected = OperandoCommunicationException.class)
	public void testGetBdaReport_FailedGet_HttpExceptionThrown() throws OperandoCommunicationException, OperandoAuthenticationException
	{
		// Set up
		String jobId = "C456";
		String serviceTicket = "abcd";
		when(mockClientAuthenticationApiOperandoClient.requestServiceTicket(anyString())).thenReturn(serviceTicket);
		String userId = "D000";
		String endpoint = ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID.replace("{job_id}", jobId);
		String httpMethod = HttpMethod.GET;
		stub(httpMethod, endpoint, "", Status.INTERNAL_SERVER_ERROR);
		
		// Exercise
		client.getBdaReport(jobId, userId);
	}
	
	@Test
	public void testGetBdaReport_SuccessfulGet_ResponseBodyInterpretedCorrectly() throws OperandoCommunicationException, OperandoAuthenticationException
	{
		// Set up
		String jobId = "C456";
		String serviceTicket = "abcd";
		when(mockClientAuthenticationApiOperandoClient.requestServiceTicket(anyString())).thenReturn(serviceTicket);
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
