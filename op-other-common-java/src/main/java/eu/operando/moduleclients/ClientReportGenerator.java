package eu.operando.moduleclients;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.NotImplementedException;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import eu.operando.OperandoCommunicationException;
import eu.operando.OperandoCommunicationException.CommunicationError;
import eu.operando.api.model.ReportOperando;

public class ClientReportGenerator extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_WEBUI_REPORTS = PATH_OPERANDO_WEBUI + "/reports";
	private static final String ENDPOINT_REPORT_GENERATOR_REPORTS = PATH_INTERNAL_OPERANDO_WEBUI_REPORTS + "/reports";

	// Query parameter names
	private static final String PARAMETER_NAME_REPORT_FORMAT = "_format";
	private static final String PARAMETER_NAME_REPORT_ID = "_reportID";

	public ClientReportGenerator(String originReportGenerator)
	{
		super(originReportGenerator);
	}

	/**
	 * Get a report from the report generator.
	 * 
	 * @param reportId
	 *        the ID of the report.
	 * @param format
	 *        the desired format.
	 * @param parametersOptional
	 *        any optional parameters particular to the report.
	 * @return the requested report.
	 * @throws OperandoCommunicationException
	 *         if for some reason a report cannot be returned.
	 */
	public ReportOperando getReport(String reportId, String format, MultivaluedMap<String, String> parametersOptional) throws OperandoCommunicationException
	{
		// Send the request.
		MultivaluedStringMap queryParams = new MultivaluedStringMap(parametersOptional);
		queryParams.add(PARAMETER_NAME_REPORT_ID, reportId);
		queryParams.add(PARAMETER_NAME_REPORT_FORMAT, format);
		Response response = sendRequestWithQueryParameters(HttpMethod.GET, ENDPOINT_REPORT_GENERATOR_REPORTS, queryParams);

		// Respond appropriately to RG's response.
		int statusCode = response.getStatus();
		if (statusCode == Status.OK.getStatusCode())
		{
			return null;
		}
		else
		{
			throw determineAppropriateOperandoCommunicationException(statusCode);
		}
	}

	/**
	 * Reads in a status code, and determines what the appropriate response is based on that.
	 * 
	 * @param statusCode
	 *        the status code on the response from the RG.
	 * @return an appropriate exception based on the status code.
	 */
	private OperandoCommunicationException determineAppropriateOperandoCommunicationException(int statusCode)
	{
		if (statusCode == Status.NOT_FOUND.getStatusCode())
		{
			return new OperandoCommunicationException(CommunicationError.REQUESTED_RESOURCE_NOT_FOUND);
		}
		else if (statusCode == Status.INTERNAL_SERVER_ERROR.getStatusCode())
		{
			return new OperandoCommunicationException(CommunicationError.ERROR_FROM_OTHER_MODULE);
		}
		else
		{
			throw new NotImplementedException("RG returned a response with status code " + statusCode
				+ ", and there is no handling for this status code. Consider updating " + ClientReportGenerator.class + " to handle this status code.");
		}
	}
}
