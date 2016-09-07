package eu.operando.moduleclients;

import java.lang.reflect.Type;
import java.util.Vector;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import com.google.common.reflect.TypeToken;

import eu.operando.api.model.PrivacySetting;

public class ClientUserDeviceEnforcement extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_DEVICE_ENFORCEMENT = "/operando/core/device_enforcement";
	public static final String ENDPOINT_USER_DEVICE_ENFORCEMENT_PRIVACY_SETTINGS = PATH_INTERNAL_OPERANDO_CORE_DEVICE_ENFORCEMENT + "/privacy_settings";

	public ClientUserDeviceEnforcement(String originUserDeviceEnforcement)
	{
		super(originUserDeviceEnforcement);
	}

	public Vector<PrivacySetting> getPrivacySettingsCurrent(int userId, int ospId)
	{
		MultivaluedMap<String, String> queryParamaters = new MultivaluedStringMap();
		queryParamaters.putSingle("user_id", "" + userId);
		queryParamaters.putSingle("osp_id", "" + ospId);

		Response response = sendRequestWithQueryParameters(HttpMethod.GET, ENDPOINT_USER_DEVICE_ENFORCEMENT_PRIVACY_SETTINGS, queryParamaters);
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
