package eu.operando;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

/**
 * This class represents a client for one of the API modules, i.e. the Regulator API or OSP API.
 * @see ClientOperandoModule
 */
public abstract class ClientOperandoModuleApi extends ClientOperandoModuleExternal
{
	private String protocolAndHostOspEnforcement; //TODO - for the moment this is only used by the RAPI, but will be used by the OSP API for the MVP.
	private String protocolAndHostReportGenerator;

	public ClientOperandoModuleApi(String protocolAndHostAuthenticationApi, String protocolAndHostLogDb, String protocolAndHostOspEnforcement, String protocolAndHostReportGenerator)
	{
		super(protocolAndHostAuthenticationApi, protocolAndHostLogDb);
		this.protocolAndHostOspEnforcement = protocolAndHostOspEnforcement;
		this.protocolAndHostReportGenerator = protocolAndHostReportGenerator;
	}

	/**
	 * Report Generator
	 */
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

		//Send the request.
		Builder requestBuilder = target.request();
		requestBuilder.get();
	}

	public String getProtocolAndHostOspEnforcement()
	{
		return protocolAndHostOspEnforcement;
	}
}
