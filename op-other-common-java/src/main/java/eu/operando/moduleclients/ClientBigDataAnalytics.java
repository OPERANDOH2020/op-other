package eu.operando.moduleclients;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import eu.operando.HttpUtils;
import eu.operando.OperandoAuthenticationException;
import eu.operando.OperandoCommunicationException;
import eu.operando.OperandoCommunicationException.CommunicationError;
import eu.operando.api.model.AnalyticsReport;

public class ClientBigDataAnalytics extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_BIGDATA = PATH_OPERANDO_CORE + "/bigdata";
	public static final String ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID = 
		PATH_INTERNAL_OPERANDO_CORE_BIGDATA + "/jobs/{job-id}/reports/latest";
	
	private static final String SERVICE_ID_BIG_DATA_ANALYTICS = "GET/osp/bda/jobs/.*/reports";
	
	private ClientAuthenticationApiOperandoClient clientAuthenticationApi = null;
	
	public ClientBigDataAnalytics(String originBigDataAnalytics, ClientAuthenticationApiOperandoClient clientAuthenticationService)
	{
		super(originBigDataAnalytics);
		clientAuthenticationApi = clientAuthenticationService; ;
	}

	public AnalyticsReport getBdaReport(String jobId, String userId) throws OperandoCommunicationException, OperandoAuthenticationException{
		String endpoint = ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID.replace("{job-id}", jobId);
		MultivaluedStringMap headers = new MultivaluedStringMap();
		headers.add("psp-user", userId);
		
		String serviceTicket = requestServiceTicket(SERVICE_ID_BIG_DATA_ANALYTICS);
		
		headers.add("service-ticket", serviceTicket);
		Response response =  sendRequest(HttpMethod.GET, endpoint, headers, null, new MultivaluedStringMap());

		// Only try to interpret the response if the status code is a success code.
		int statusCodeResponse = response.getStatus();
		boolean responseSuccessful = HttpUtils.statusCodeIsInFamily(statusCodeResponse, Status.Family.SUCCESSFUL);
		if (!responseSuccessful)
		{
			throw new OperandoCommunicationException(CommunicationError.OTHER, "A response from the BDA had status code: " + statusCodeResponse);
		}

		String strJson = response.readEntity(String.class);
		return createObjectFromJsonFollowingOperandoConventions(strJson, AnalyticsReport.class);
	}
	
	private String requestServiceTicket(String serviceId) throws OperandoAuthenticationException
	{
		return clientAuthenticationApi.requestServiceTicket(serviceId);
	}
}
