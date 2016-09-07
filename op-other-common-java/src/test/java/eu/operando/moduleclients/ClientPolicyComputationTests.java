package eu.operando.moduleclients;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpException;
import org.junit.Test;

import eu.operando.api.model.PrivacyRegulation;
import eu.operando.api.model.PrivacyRegulationInput;
import eu.operando.api.model.DtoPrivacyRegulation.PrivateInformationTypeEnum;
import eu.operando.api.model.DtoPrivacyRegulation.RequiredConsentEnum;

public class ClientPolicyComputationTests extends ClientTests
{
	// Variables to be tested.
	private static final String PATH_INTERNAL_OPERANDO_CORE_POLICY_COMPUTATION = "/operando/core/policy_computation";
	private static final String ENDPOINT_POLICY_COMPUTATION_REGULATIONS = PATH_INTERNAL_OPERANDO_CORE_POLICY_COMPUTATION + "/regulations";
	private static final String ENDPOINT_POLICY_COMPUTATION_REGULATIONS_VARIABLE_REG_ID = ENDPOINT_POLICY_COMPUTATION_REGULATIONS + "/{reg_id}";

	// Dummy variables for facilitating testing.
	private static final PrivacyRegulation REGULATION =
			new PrivacyRegulation("1", "sector", "source", PrivateInformationTypeEnum.BEHAVIOURAL, "action", RequiredConsentEnum.IN);
	private static final PrivacyRegulationInput INPUT_OBJECT = REGULATION.getInputObject();
	private static final String ENDPOINT_EXISTING_REGULATION_WITH_REG_ID =
			ENDPOINT_POLICY_COMPUTATION_REGULATIONS_VARIABLE_REG_ID.replace("{reg_id}", REGULATION.getRegId());

	private ClientPolicyComputation client = new ClientPolicyComputation(ORIGIN_WIREMOCK);

	@Test
	public void testSendNewRegulationToPolicyComputation_CorrectHttpRequest() throws HttpException
	{
		// Exercise
		client.sendNewRegulationToPolicyComputation(REGULATION);

		// Verify
		verifyCorrectHttpRequest(HttpMethod.POST, ENDPOINT_POLICY_COMPUTATION_REGULATIONS, INPUT_OBJECT);
	}

	@Test
	public void testSendNewRegulationToPolicyComputation_FailureFromPolicyComputation_FalseReturned() throws HttpException
	{
		// Set up
		stub(HttpMethod.POST, ENDPOINT_POLICY_COMPUTATION_REGULATIONS, "", Status.NOT_FOUND);

		// Exercise
		boolean success = client.sendNewRegulationToPolicyComputation(REGULATION);

		// Verify
		assertEquals("When PC fails to process the regulation, the RAPI client should return false", false, success);
	}

	@Test
	public void testSendNewRegulationToPolicyComputation_SuccessFromPolicyComputation_TrueReturned() throws HttpException
	{
		// Set up
		stub(HttpMethod.POST, ENDPOINT_POLICY_COMPUTATION_REGULATIONS, "", Status.ACCEPTED);

		// Exercise
		boolean success = client.sendNewRegulationToPolicyComputation(REGULATION);

		// Verify
		assertEquals("When PC successfully processes the regulation, the RAPI client should return true", true, success);
	}

	@Test
	public void testSendExistingRegulationToPolicyComputation_CorrectHttpRequest() throws HttpException
	{
		// Exercise
		client.sendExistingRegulationToPolicyComputation(REGULATION);

		// Verify
		verifyCorrectHttpRequest(HttpMethod.PUT, ENDPOINT_EXISTING_REGULATION_WITH_REG_ID, INPUT_OBJECT);
	}

	@Test
	public void testSendExistingRegulationToPolicyComputation_FailureFromPolicyComputation_FalseReturned() throws HttpException
	{
		// Set up
		stub(HttpMethod.PUT, ENDPOINT_EXISTING_REGULATION_WITH_REG_ID, "", Status.NOT_FOUND);

		// Exercise
		boolean success = client.sendExistingRegulationToPolicyComputation(REGULATION);

		// Verify
		assertEquals("When PC fails to process the regulation, the RAPI client should return false", false, success);
	}

	@Test
	public void testSendExistingRegulationToPolicyComputation_SuccessFromPolicyComputation_TrueReturned() throws HttpException
	{
		// Set up
		stub(HttpMethod.PUT, ENDPOINT_EXISTING_REGULATION_WITH_REG_ID, "", Status.ACCEPTED);

		// Exercise
		boolean success = client.sendExistingRegulationToPolicyComputation(REGULATION);

		// Verify
		assertEquals("When PC successfully processes the regulation, the RAPI client should return true", true, success);
	}
}
