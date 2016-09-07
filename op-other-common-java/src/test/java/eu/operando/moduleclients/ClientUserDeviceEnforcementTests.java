package eu.operando.moduleclients;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Vector;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Test;

import eu.operando.api.model.PrivacySetting;

public class ClientUserDeviceEnforcementTests extends ClientTests
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_DEVICE_ENFORCEMENT = "/operando/core/device_enforcement";
	public static final String ENDPOINT_USER_DEVICE_ENFORCEMENT_PRIVACY_SETTINGS = PATH_INTERNAL_OPERANDO_CORE_DEVICE_ENFORCEMENT + "/privacy_settings";

	private ClientUserDeviceEnforcement client = new ClientUserDeviceEnforcement(ORIGIN_WIREMOCK);

	@Test
	public void testGetPrivacySettingsCurrent_CorrectHttpRequest()
	{
		// Set Up
		String endpoint = ENDPOINT_USER_DEVICE_ENFORCEMENT_PRIVACY_SETTINGS;
		stub(HttpMethod.GET, endpoint);

		int userId = 1;
		int ospId = 2;

		// Exercise
		client.getPrivacySettingsCurrent(userId, ospId);

		// Verify
		MultivaluedMap<String, String> queryParamaters = new MultivaluedStringMap();
		queryParamaters.putSingle("user_id", "" + userId);
		queryParamaters.putSingle("osp_id", "" + ospId);
		verifyCorrectHttpRequestWithQueryParams(HttpMethod.GET, endpoint, queryParamaters);
	}

	@Test
	public void testGetCurrentPrivacySettings_HandlesSuccessfulResponseCorrectly()
	{
		// Set up
		Vector<PrivacySetting> settingsExpected = createTestVectorPrivacySettings();
		stub(HttpMethod.GET, ENDPOINT_USER_DEVICE_ENFORCEMENT_PRIVACY_SETTINGS, settingsExpected);

		// Exercise
		Vector<PrivacySetting> settingsActual = client.getPrivacySettingsCurrent(3, 4);

		// Verify
		assertThat(settingsActual, is(equalTo(settingsExpected)));
	}

	private Vector<PrivacySetting> createTestVectorPrivacySettings()
	{
		// Create a vector of multiple privacy settings.
		Vector<PrivacySetting> vSettingsExpected = new Vector<PrivacySetting>();
		PrivacySetting settingOne = new PrivacySetting(1, "descOne", "nameOne", "keyOne", "valueOne");
		PrivacySetting settingTwo = new PrivacySetting(2, "descTwo", "nameTwo", "keyTwo", "valueTwo");
		vSettingsExpected.add(settingOne);
		vSettingsExpected.add(settingTwo);
		return vSettingsExpected;
	}
}
