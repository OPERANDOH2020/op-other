package eu.operando.moduleclients;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Vector;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Test;

import eu.operando.api.model.DtoPrivacyRegulation.PrivateInformationTypeEnum;
import eu.operando.api.model.DtoPrivacyRegulation.RequiredConsentEnum;
import eu.operando.api.model.PrivacyRegulation;
import eu.operando.api.model.PrivacyRegulationInput;
import eu.operando.api.model.PrivacySetting;

public class ClientOspEnforcementTests extends ClientOperandoModuleTests
{
	// Variables to be tested.
	private static final String ENDPOINT_OSP_ENFORCEMENT_REGULATIONS = "/regulations";
	private static final String PATH_PARAMETER_REG_ID = "{reg_id}";
	private static final String ENDPOINT_OSP_ENFORCEMENT_REGULATIONS_VARIABLE_REG_ID = ENDPOINT_OSP_ENFORCEMENT_REGULATIONS + "/" + PATH_PARAMETER_REG_ID;
	private static final String PATH_PARAMETER_OSP_ID = "{osp_id}";
	private static final String ENDPOINT_OSP_ENFORCEMENT_PRIVACY_SETTINGS_VARIABLE_OSP_ID =
			"/osps/" + PATH_PARAMETER_OSP_ID + "/privacy_settings";

	// Dummy variables for facilitating testing - regulations.
	private static final PrivacyRegulation REGULATION =
			new PrivacyRegulation("1", "sector", "source", PrivateInformationTypeEnum.BEHAVIOURAL, "action", RequiredConsentEnum.IN);
	private static final PrivacyRegulationInput INPUT_OBJECT = REGULATION.getInputObject();
	private static final String ENDPOINT_EXISTING_REGULATION_WITH_REG_ID =
			ENDPOINT_OSP_ENFORCEMENT_REGULATIONS_VARIABLE_REG_ID.replace(PATH_PARAMETER_REG_ID, REGULATION.getRegId());
	// Dummy variables for facilitating testing - privacy settings.
	private static final int USER_ID = 1;
	private static final int OSP_ID = 2;
	private static final String ENDPOINT_OSP_ENFORCEMENT_PRIVACY_SETTINGS_WITH_OSP_ID =
			ENDPOINT_OSP_ENFORCEMENT_PRIVACY_SETTINGS_VARIABLE_OSP_ID.replace(PATH_PARAMETER_OSP_ID, "" + OSP_ID);

	private ClientOspEnforcement client = new ClientOspEnforcement(ORIGIN_WIREMOCK);

	@Test
	public void testSendNewRegulationToOspEnforcement_CorrectHttpRequest()
	{
		// Exercise
		client.sendNewRegulationToOspEnforcement(REGULATION);

		// Verify
		verifyCorrectHttpRequest(HttpMethod.POST, ENDPOINT_OSP_ENFORCEMENT_REGULATIONS, REGULATION);
	}

	@Test
	public void testSendNewRegulationToOspEnforcement_FailureFromPolicyComputation_FalseReturned()
	{
		// Set up
		stub(HttpMethod.POST, ENDPOINT_OSP_ENFORCEMENT_REGULATIONS, "", Status.NOT_FOUND);

		// Exercise
		boolean success = client.sendNewRegulationToOspEnforcement(REGULATION);

		// Verify
		assertEquals("When PC fails to process the regulation, the RAPI client should return false", false, success);
	}

	@Test
	public void testSendNewRegulationToOspEnforcement_SuccessFromPolicyComputation_TrueReturned()
	{
		// Set up
		stub(HttpMethod.POST, ENDPOINT_OSP_ENFORCEMENT_REGULATIONS, "", Status.CREATED);

		// Exercise
		boolean success = client.sendNewRegulationToOspEnforcement(REGULATION);

		// Verify
		assertEquals("When PC successfully processes the regulation, the RAPI client should return true", true, success);
	}

	@Test
	public void testSendExistingRegulationToOspEnforcement_CorrectHttpRequest()
	{
		// Exercise
		client.sendExistingRegulationToOspEnforcement(REGULATION);

		// Verify
		verifyCorrectHttpRequest(HttpMethod.PUT, ENDPOINT_EXISTING_REGULATION_WITH_REG_ID, INPUT_OBJECT);
	}

	@Test
	public void testSendExistingRegulationToOspEnforcement_FailureFromPolicyComputation_FalseReturned()
	{
		// Set up
		stub(HttpMethod.PUT, ENDPOINT_EXISTING_REGULATION_WITH_REG_ID, "", Status.NOT_FOUND);

		// Exercise
		boolean success = client.sendExistingRegulationToOspEnforcement(REGULATION);

		// Verify
		assertEquals("When PC fails to process the regulation, the RAPI client should return false", false, success);
	}

	@Test
	public void testSendExistingRegulationToOspEnforcement_SuccessFromPolicyComputation_TrueReturned()
	{
		// Set up
		stub(HttpMethod.PUT, ENDPOINT_EXISTING_REGULATION_WITH_REG_ID, "", Status.CREATED);

		// Exercise
		boolean success = client.sendExistingRegulationToOspEnforcement(REGULATION);

		// Verify
		assertEquals("When PC successfully processes the regulation, the RAPI client should return true", true, success);
	}

	@Test
	public void testGetPrivacySettingsRequired_CorrectHttpRequest()
	{
		// Set Up
		stub(HttpMethod.GET, ENDPOINT_OSP_ENFORCEMENT_PRIVACY_SETTINGS_WITH_OSP_ID);

		// Exercise
		client.getPrivacySettingsRequired(USER_ID, OSP_ID);

		// Verify
		MultivaluedMap<String, String> queriesParamToValue = new MultivaluedStringMap();
		queriesParamToValue.putSingle("user_id", "" + USER_ID);
		verifyCorrectHttpRequestWithQueryParams(HttpMethod.GET, ENDPOINT_OSP_ENFORCEMENT_PRIVACY_SETTINGS_WITH_OSP_ID, queriesParamToValue);
	}

	@Test
	public void testGetPrivacySettingsRequired_HandlesSuccessfulResponseCorrectly()
	{
		// Set Up
		Vector<PrivacySetting> settingsExpected = createTestVectorPrivacySettings();

		stub(HttpMethod.GET, ENDPOINT_OSP_ENFORCEMENT_PRIVACY_SETTINGS_WITH_OSP_ID, settingsExpected);

		// Exercise
		Vector<PrivacySetting> settingsActual = client.getPrivacySettingsRequired(USER_ID, OSP_ID);

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
