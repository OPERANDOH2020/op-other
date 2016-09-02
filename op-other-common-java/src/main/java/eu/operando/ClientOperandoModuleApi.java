package eu.operando;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.NotImplementedException;

import eu.operando.OperandoCommunicationException.CommunicationError;

/**
 * This class represents a client for one of the API modules, i.e. the Regulator API or OSP API.
 * 
 * @see ClientOperandoModule
 */
public abstract class ClientOperandoModuleApi extends ClientOperandoModuleExternal
{
	private String originOspEnforcement; // TODO - for the moment this is only used by the RAPI, but will be used by the OSP API for the MVP.
	private String originReportGenerator;

	public ClientOperandoModuleApi(String originAuthenticationApi, String originLogDb, String originOspEnforcement, String originReportGenerator)
	{
		super(originAuthenticationApi, originLogDb);
		this.originOspEnforcement = originOspEnforcement;
		this.originReportGenerator = originReportGenerator;
	}

	/**
	 * Report Generator
	 */
	public ReportOperando getReport(String reportId, String format, MultivaluedMap<String, String> parametersOptional) throws OperandoCommunicationException
	{
		// Create a web target for the correct end-point.
		WebTarget target = getClient().target(originReportGenerator);
		target = target.path(ENDPOINT_REPORT_GENERATOR_REPORTS);
		target = target.queryParam("_reportID", reportId);
		target = target.queryParam("_format", format);
		target = addQueryParametersOptional(target, parametersOptional);

		// Send the request.
		Builder requestBuilder = target.request();
		Response response = requestBuilder.get();

		// Respond appropriately to RG's response.
		int statusCode = response.getStatus();
		if (statusCode == Status.OK.getStatusCode())
		{
			// TODO - need to know how a report is represented.
			return null;
		}
		else
		{
			throw determineAppropriateOperandoCommunicationException(statusCode);
		}
	}

	/**
	 * Add query parameters to a web target.
	 * 
	 * @param target
	 *        the web target to add the query parameters to.
	 * @param parametersOptional
	 *        a map from parameter names to the values against that name.
	 * @return
	 */
	private WebTarget addQueryParametersOptional(WebTarget target, MultivaluedMap<String, String> parametersOptional)
	{
		// Iterate over the set of parameter names.
		Set<String> parameterNames = parametersOptional.keySet();
		Iterator<String> iteratorParameterNames = parameterNames.iterator();
		while (iteratorParameterNames.hasNext())
		{
			// Iterate over the list of values corresponding to a particular parameter name.
			String parameterName = iteratorParameterNames.next();
			List<String> parameterValues = parametersOptional.get(parameterName);
			Iterator<String> iteratorParameterValues = parameterValues.iterator();
			while (iteratorParameterValues.hasNext())
			{
				// Add each parameter value as a query parameter against the parameter name.
				String parameterValue = iteratorParameterValues.next();
				target = target.queryParam(parameterName, parameterValue);
			}
		}

		return target;
	}

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
			throw new NotImplementedException("RG returned a response with status code "
				+ statusCode
				+ ", and there is no handling for this status code. Consider updating "
				+ ClientOperandoModuleApi.class
				+ " to handle this status code.");
		}
	}

	protected String getOriginOspEnforcement()
	{
		return originOspEnforcement;
	}
}