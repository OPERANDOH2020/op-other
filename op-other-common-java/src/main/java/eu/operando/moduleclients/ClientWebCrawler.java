package eu.operando.moduleclients;

import java.lang.reflect.Type;
import java.util.Vector;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;

import com.google.common.reflect.TypeToken;

import eu.operando.api.model.PrivacyPolicy;

public class ClientWebCrawler extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_INTERFACES_WEB_SERVICES = PATH_OPERANDO_INTERFACES + "/web_services";
	public static final String ENDPOINT_WEB_SERVICES_PRIVACY_POLICIES = PATH_INTERNAL_OPERANDO_INTERFACES_WEB_SERVICES + "/OSPPrivacyTerms";
	public static final String ENDPOINT_WEB_SERVICES_PRIVACY_OPTIONS = PATH_INTERNAL_OPERANDO_INTERFACES_WEB_SERVICES + "/OSPPrivacySettings";
	
	public ClientWebCrawler(String originWebCrawler)
	{
		super(originWebCrawler);
	}

	public Vector<PrivacyPolicy> getOspPrivacyPolicies()
	{
		Response response = sendRequest(HttpMethod.GET, ENDPOINT_WEB_SERVICES_PRIVACY_POLICIES);
		String stringJson = response.readEntity(String.class);

		@SuppressWarnings("serial")
		Type type = new TypeToken<Vector<PrivacyPolicy>>(){}.getType();
		return createObjectFromJsonFollowingOperandoConventions(stringJson, type);
	}

	public void getOspPrivacyOptions()
	{
		sendRequest(HttpMethod.GET, ENDPOINT_WEB_SERVICES_PRIVACY_OPTIONS);
	}
}
