package eu.operando;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.util.HashMap;

/**
 * Class which tests the code in OperandoApiModuleClient.
 */
public abstract class ClientOperandoModuleApiTests extends ClientOperandoModuleExternalTests
{
	public void testGetReport_NoOptionalParameters_CorrectHttpRequest(ClientOperandoModuleApi client)
	{
		//Set up
		String endpoint = ENDPOINT_REPORT_GENERATOR_REPORTS;
		int reportId = 1234;
		String format = "pdf";
		HashMap<String, String> parametersOptional = new HashMap<String, String>();
		
		getWireMockRule().stubFor(get(urlPathEqualTo(endpoint))
				.withQueryParam("_reportID", equalTo("" + reportId))
				.withQueryParam("_format", equalTo(format))
				.willReturn(aResponse()));
		
		//Exercise
		client.getReport(reportId, format, parametersOptional);
		
		//Verify
		getWireMockRule().verify(getRequestedFor(urlPathEqualTo(endpoint))
				.withQueryParam("_reportID", equalTo("" + reportId))
				.withQueryParam("_format", equalTo(format)));
		
	}
	
	public void testGetReport_TwoOptionalParameters_CorrectHttpRequest(ClientOperandoModuleApi client)
	{
		//Set up
		String endpoint = ENDPOINT_REPORT_GENERATOR_REPORTS;
		int reportId = 1234;
		String format = "pdf";
		HashMap<String, String> parametersOptional = new HashMap<String, String>();
		String parameterOneName = "param1";
		String parameterOneValue = "xxx";
		String parameterTwoName = "param2";
		String parameterTwoValue = "yyy";
		parametersOptional.put(parameterOneName, parameterOneValue);
		parametersOptional.put(parameterTwoName, parameterTwoValue);
		
		getWireMockRule().stubFor(get(urlPathEqualTo(endpoint))
				.withQueryParam("_reportID", equalTo("" + reportId))
				.withQueryParam("_format", equalTo(format))
				.withQueryParam(parameterOneName, equalTo(parameterOneValue))
				.withQueryParam(parameterTwoName, equalTo(parameterTwoValue))
				.willReturn(aResponse()));
		
		//Exercise
		client.getReport(reportId, format, parametersOptional);
		
		//Verify
		getWireMockRule().verify(getRequestedFor(urlPathEqualTo(endpoint))
				.withQueryParam("_reportID", equalTo("" + reportId))
				.withQueryParam("_format", equalTo(format))
				.withQueryParam(parameterOneName, equalTo(parameterOneValue))
				.withQueryParam(parameterTwoName, equalTo(parameterTwoValue)));	
	}
}
