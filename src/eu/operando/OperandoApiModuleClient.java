package eu.operando;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.client.Invocation.Builder;

public abstract class OperandoApiModuleClient extends OperandoModuleClient
{
	private String protocolAndHostAuthenticationService;
	private String protocolAndHostOspEnforcement;
	private String protocolAndHostReportGenerator;
	private String protocolAndHostLogDb;

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
