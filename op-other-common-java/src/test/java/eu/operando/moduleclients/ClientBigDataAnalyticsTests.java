package eu.operando.moduleclients;

import static org.junit.Assert.fail;

import javax.ws.rs.HttpMethod;

import org.junit.Test;

public class ClientBigDataAnalyticsTests extends ClientOperandoModuleTests
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_BIGDATA = "/operando/core/bigdata";
	public static final String ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID = PATH_INTERNAL_OPERANDO_CORE_BIGDATA + "/reports/%d";
	
	private ClientBigDataAnalytics client = new ClientBigDataAnalytics(ORIGIN_WIREMOCK);
	
	@Test
	public void testGetBdaReport_CorrectHttpRequest()
	{
		fail("class not ready for testing");
//		// Set Up
//		int ospId = 1;
//
//		// Exercise
//		client.getBdaReport(ospId);
//
//		// Verify
//		String endpoint = String.format(ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID, ospId);
//		verifyCorrectHttpRequest(HttpMethod.GET, endpoint);
	}
}
