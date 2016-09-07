package eu.operando.moduleclients;

import javax.ws.rs.HttpMethod;

public class ClientBigDataAnalytics extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_BIGDATA = PATH_OPERANDO_CORE + "/bigdata";
	public static final String ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID = PATH_INTERNAL_OPERANDO_CORE_BIGDATA + "/reports/%d";
	
	public ClientBigDataAnalytics(String originBigDataAnalytics)
	{
		super(originBigDataAnalytics);
	}

	public void getBdaReport(int reportId)
	{
		String endpoint = String.format(ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID, reportId);
		sendRequest(HttpMethod.GET, endpoint);
	}
}
