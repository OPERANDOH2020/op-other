package eu.operando.moduleclients;

import eu.operando.OperandoCommunicationException;
import eu.operando.api.model.AnalyticsReport;

public class ClientBigDataAnalytics extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_BIGDATA = PATH_OPERANDO_CORE + "/bigdata";
	public static final String ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID = PATH_INTERNAL_OPERANDO_CORE_BIGDATA + "/reports/%d";
	
	public ClientBigDataAnalytics(String originBigDataAnalytics)
	{
		super(originBigDataAnalytics);
	}

	// TODO implementation
	public AnalyticsReport getBdaReport(String jobId, String userId) throws OperandoCommunicationException{
		return null;
	}
}
