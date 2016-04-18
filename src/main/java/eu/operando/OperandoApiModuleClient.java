package eu.operando;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

/**
 * This class represents a client for one of the API modules, i.e. the Regulator API or OSP API.
 * @see OperandoModuleClient 
 */
public abstract class OperandoApiModuleClient extends OperandoModuleClient
{
	private String protocolAndHostAuthenticationService; //TODO - implement
	private String protocolAndHostOspEnforcement;
	private String protocolAndHostReportGenerator;
	private String protocolAndHostLogDb; //TODO - implement

	 /**
	  * @see String#equals(Object) equals
	  */
	public OperandoApiModuleClient(String protocolAndHostAuthenticationService, String protocolAndHostOspEnforcement, String protocolAndHostReportGenerator, String protocolAndHostLogDb)
	{
		this.protocolAndHostAuthenticationService = protocolAndHostAuthenticationService;
		this.protocolAndHostOspEnforcement = protocolAndHostOspEnforcement;
		this.protocolAndHostReportGenerator = protocolAndHostReportGenerator;
		this.protocolAndHostLogDb = protocolAndHostLogDb;
	}

	public void getReport(int reportId, String format, HashMap<String, String> parametersOptional)
	{
		//Create a web target for the correct end-point.
		WebTarget target = getClient().target(protocolAndHostReportGenerator);
		target = target.path(ENDPOINT_REPORT_GENERATOR_REPORTS);
		target = target.queryParam("_reportID", reportId);
		target = target.queryParam("_format", format);
		
		Set<Entry<String, String>> entrySet = parametersOptional.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext())
		{
			Entry<String, String> parameter = iterator.next();
			String key = parameter.getKey();
			String value = parameter.getValue();
			target = target.queryParam(key, value);
		}

		//Send the request with the regulation encoded as JSON in the body.
		Builder requestBuilder = target.request();
		requestBuilder.get();
	}

	public String getProtocolAndHostOspEnforcement()
	{
		return protocolAndHostOspEnforcement;
	}
}
