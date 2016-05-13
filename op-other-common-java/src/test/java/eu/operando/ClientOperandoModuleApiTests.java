package eu.operando;

import java.util.HashMap;

import javax.ws.rs.HttpMethod;

/**
 * Class which tests the code in OperandoApiModuleClient.
 */
public abstract class ClientOperandoModuleApiTests extends ClientOperandoModuleExternalTests
{
	public void testGetReport_NoOptionalParameters_CorrectHttpRequest(ClientOperandoModuleApi client)
	{
		//Set up
		int reportId = 1234;
		String format = "pdf";
		
		//Exercise
		client.getReport(reportId, format, new HashMap<String, String>());
		
		//Verify
		HashMap<String, String> queriesParamToValue = new HashMap<String, String>();
		queriesParamToValue.put("_reportID", "" + reportId);
		queriesParamToValue.put("_format", "" + format);
		verifyCorrectHttpRequestWithoutBody(HttpMethod.GET, ENDPOINT_REPORT_GENERATOR_REPORTS, queriesParamToValue);
		
	}
	
	public void testGetReport_TwoOptionalParameters_CorrectHttpRequest(ClientOperandoModuleApi client)
	{
		//Set up
		int reportId = 1234;
		String format = "pdf";
		HashMap<String, String> parametersOptional = new HashMap<String, String>();
		String parameterOneName = "param1";
		String parameterOneValue = "xxx";
		String parameterTwoName = "param2";
		String parameterTwoValue = "yyy";
		parametersOptional.put(parameterOneName, parameterOneValue);
		parametersOptional.put(parameterTwoName, parameterTwoValue);
		
		//Exercise
		client.getReport(reportId, format, parametersOptional);
		
		//Verify
		HashMap<String, String> queriesParamToValue = new HashMap<String, String>();
		queriesParamToValue.put("_reportID", "" + reportId);
		queriesParamToValue.put("_format", "" + format);
		queriesParamToValue.put(parameterOneName, parameterOneValue);
		queriesParamToValue.put(parameterTwoName, parameterTwoValue);
		verifyCorrectHttpRequestWithoutBody(HttpMethod.GET, ENDPOINT_REPORT_GENERATOR_REPORTS, queriesParamToValue);
	}
}
