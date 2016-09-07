package eu.operando.moduleclients;

import java.lang.reflect.Type;
import java.util.Vector;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import com.google.common.reflect.TypeToken;

import eu.operando.api.model.PrivacyRegulation;
import eu.operando.api.model.PrivacySetting;

public class ClientOspEnforcement extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_OSP_ENFORCEMENT = PATH_OPERANDO_CORE + "/osp_enforcement";
	private static final String ENDPOINT_OSP_ENFORCEMENT_REGULATIONS = PATH_INTERNAL_OPERANDO_CORE_OSP_ENFORCEMENT + "/regulations";
	private static final String ENDPOINT_OSP_ENFORCEMENT_REGULATIONS_VARIABLE_REG_ID = ENDPOINT_OSP_ENFORCEMENT_REGULATIONS + "/{reg_id}";
	public static final String ENDPOINT_OSP_ENFORCEMENT_PRIVACY_SETTINGS_VARIABLE_OSP_ID = PATH_INTERNAL_OPERANDO_CORE_OSP_ENFORCEMENT + "/osps/%d/privacy_settings";
	
	public ClientOspEnforcement(String originOspEnforcement)
	{
		super(originOspEnforcement);
	}

	/**
	 * Inform PC of a new regulation.
	 * @param regulation
	 * 	The regulation.
	 * @return
	 * 	Whether the request was accepted.
	 */
	public boolean sendNewRegulationToOspEnforcement(PrivacyRegulation regulation)
	{
		Response responseFromPc = sendRequest(HttpMethod.POST, ENDPOINT_OSP_ENFORCEMENT_REGULATIONS, regulation.getInputObject());
		
		return wasOspEnforcementRequestSuccessful(responseFromPc);
	}

	/**
	 * Inform PC that a regulation has been updated.
	 * @param regulation
	 * 	The regulation.
	 * @return
	 * 	Whether the request was accepted.
	 */
	public boolean sendExistingRegulationToOspEnforcement(PrivacyRegulation regulation)
	{
		String regId = regulation.getRegId();
		String endpoint = ENDPOINT_OSP_ENFORCEMENT_REGULATIONS_VARIABLE_REG_ID.replace("{reg_id}", regId);
		Response responseFromPc = sendRequest(HttpMethod.PUT, endpoint, regulation.getInputObject());
		
		return wasOspEnforcementRequestSuccessful(responseFromPc);
	}

	/**
	 * Analyses a response from Policy Computation to see if it was successful. 
	 * @param responseFromPc
	 * 	the response to be analysed.
	 * @return
	 * 	whether it was successful
	 */
	private boolean wasOspEnforcementRequestSuccessful(Response responseFromPc)
	{
		int statusCodeFromPc = responseFromPc.getStatus();
		boolean requestWasSuccessful = statusCodeFromPc == Status.ACCEPTED.getStatusCode();
		return requestWasSuccessful;
	}

	public Vector<PrivacySetting> getPrivacySettingsRequired(int userId, int ospId)
	{
		MultivaluedMap<String, String> queryParamaters = new MultivaluedStringMap();
		queryParamaters.putSingle("user_id", "" + userId);

		Response response = sendRequestWithQueryParameters(HttpMethod.GET, String.format(ENDPOINT_OSP_ENFORCEMENT_PRIVACY_SETTINGS_VARIABLE_OSP_ID, ospId), queryParamaters);
		return readPrivacySettingsFromResponse(response);
	}

	private Vector<PrivacySetting> readPrivacySettingsFromResponse(Response response)
	{
		String strJson = response.readEntity(String.class);

		@SuppressWarnings("serial")
		Type type = new TypeToken<Vector<PrivacySetting>>()
		{
		}.getType();
		return createObjectFromJsonFollowingOperandoConventions(strJson, type);
	}
}
