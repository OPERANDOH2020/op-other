package eu.operando.moduleclients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Vector;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.http.HttpException;
import org.junit.Test;

import eu.operando.OperandoCommunicationException;
import eu.operando.api.model.DtoPrivacyRegulation.PrivateInformationTypeEnum;
import eu.operando.api.model.DtoPrivacyRegulation.RequiredConsentEnum;
import eu.operando.api.model.PrivacyPolicy;
import eu.operando.api.model.PrivacyPolicy.AccessPolicy;
import eu.operando.api.model.PrivacyRegulation;
import eu.operando.api.model.PrivacyRegulationInput;

public class ClientPolicyDbTests extends ClientOperandoModuleTests
{
	// Variables to test
	private static final String ENDPOINT_POLICY_DB_PRIVACY_POLICY_VARIABLE_OSP_ID = "/OSP/{osp_id}/privacy-policy";
	private static final String ENDPOINT_POLICY_DB_REGULATIONS = "/regulations";
	private static final String ENDPOINT_POLICY_DB_REGULATIONS_VARIABLE_REG_ID = ENDPOINT_POLICY_DB_REGULATIONS + "/{reg_id}";

	// Dummy variables to assist testing.
	private static final String OSP_ID_TO_SEND = "1";
	private static final Vector<PrivacyPolicy.AccessPolicy> ACCESS_POLICIES =
			new Vector<>(Arrays.asList(new AccessPolicy("2", "Doctor", "Patient", "Medical Data", "Medical Reasons")));
	private static final PrivacyPolicy POLICY_RETURNED_FROM_PDB = new PrivacyPolicy("2", ACCESS_POLICIES);
	private static final PrivacyRegulation REGULATION_TO_SEND =
			new PrivacyRegulation("1", "sector", "reason", PrivateInformationTypeEnum.BEHAVIOURAL, "action", RequiredConsentEnum.IN);
	private static final PrivacyRegulation REGULATION_RETURNED_FROM_PDB = REGULATION_TO_SEND;
	private ClientPolicyDb client = new ClientPolicyDb(ORIGIN_WIREMOCK);

	@Test
	public void testGetPrivacyPolicyOnPolicyDb_CorrectHttpRequest() throws OperandoCommunicationException
	{
		// Set up
		String endpoint = ENDPOINT_POLICY_DB_PRIVACY_POLICY_VARIABLE_OSP_ID.replace("{osp_id}", OSP_ID_TO_SEND);
		String httpMethod = HttpMethod.GET;
		stub(httpMethod, endpoint, POLICY_RETURNED_FROM_PDB);

		// Exercise
		client.getPrivacyPolicyForOsp(OSP_ID_TO_SEND);

		// Verify
		verifyCorrectHttpRequest(httpMethod, endpoint);
	}

	@Test(expected = OperandoCommunicationException.class)
	public void testGetPrivacyPolicyOnPolicyDb_FailedGet_HttpExceptionThrown() throws OperandoCommunicationException
	{
		// Set up
		String endpoint = ENDPOINT_POLICY_DB_PRIVACY_POLICY_VARIABLE_OSP_ID.replace("{osp_id}", OSP_ID_TO_SEND);
		String httpMethod = HttpMethod.GET;
		stub(httpMethod, endpoint, "", Status.INTERNAL_SERVER_ERROR);

		// Exercise
		client.getPrivacyPolicyForOsp(OSP_ID_TO_SEND);
	}

	@Test
	public void testGetPrivacyPolicyOnPolicyDb_SuccessfulGet_ResponseBodyInterpretedCorrectly() throws OperandoCommunicationException
	{
		// Set up
		String endpoint = ENDPOINT_POLICY_DB_PRIVACY_POLICY_VARIABLE_OSP_ID.replace("{osp_id}", OSP_ID_TO_SEND);
		String httpMethod = HttpMethod.GET;
		stub(httpMethod, endpoint, POLICY_RETURNED_FROM_PDB);

		// Exercise
		PrivacyPolicy policyReturnedFromClient = client.getPrivacyPolicyForOsp(OSP_ID_TO_SEND);

		// Verify
		assertEquals("The client did not correctly interpret the returned JSON", POLICY_RETURNED_FROM_PDB, policyReturnedFromClient);
	}

	@Test
	public void testCreateNewRegulationOnPolicyDb_CorrectHttpRequest() throws OperandoCommunicationException
	{
		testSendRegulationToPdb_CorrectHttpRequest(true);
	}

	@Test(expected = OperandoCommunicationException.class)
	public void testCreateNewRegulationOnPolicyDb_FailedPost_HttpExceptionThrown() throws OperandoCommunicationException
	{
		testSendRegulationToPdb_ResponseBodyInterpretedCorrectly(true, false);
	}

	@Test
	public void testCreateNewRegulationOnPolicyDb_SuccessfulPost_ResponseBodyInterpretedCorrectly() throws OperandoCommunicationException
	{
		testSendRegulationToPdb_ResponseBodyInterpretedCorrectly(true, true);
	}

	@Test
	public void testUpdateExistingRegulationOnPolicyDb_CorrectHttpRequest() throws OperandoCommunicationException
	{
		testSendRegulationToPdb_CorrectHttpRequest(false);
	}

	@Test(expected = OperandoCommunicationException.class)
	public void testUpdateExistingRegulationOnPolicyDb_FailedPost_HttpExceptionThrown() throws OperandoCommunicationException
	{
		testSendRegulationToPdb_ResponseBodyInterpretedCorrectly(false, false);
	}

	/**
	 * Test the client uses a correct HTTP request when sending a regulation to another module.
	 * 
	 * @param module
	 *        the module to send the regulation to.
	 * @param newRegulation
	 *        whether the regulation is new.
	 * @throws HttpException
	 */
	private void testSendRegulationToPdb_CorrectHttpRequest(boolean newRegulation) throws OperandoCommunicationException
	{
		// Set up
		stubResponseFromPdb(newRegulation);

		// Exercise
		sendRegulationToPdb(REGULATION_TO_SEND, newRegulation);

		// Verify
		String httpMethod = determineHttpMethod(newRegulation);
		String endpointExpected = determineEndpoint(REGULATION_TO_SEND, newRegulation);
		PrivacyRegulationInput inputObject = REGULATION_TO_SEND.getInputObject();
		verifyCorrectHttpRequest(httpMethod, endpointExpected, inputObject);
	}

	/**
	 * Test the client behaves correctly when sending a regulation to the PDB.
	 * 
	 * @param newRegulation
	 *        whether the regulation is new.
	 * @param successfulRequest
	 *        whether the request should return a (stubbed) successful response.
	 * @throws HttpException
	 */
	private void testSendRegulationToPdb_ResponseBodyInterpretedCorrectly(boolean newRegulation, boolean successfulRequest) throws OperandoCommunicationException
	{
		// Set up
		stubResponseFromPdb(newRegulation, successfulRequest);

		// Exercise
		PrivacyRegulation regulationReturnedFromClient = sendRegulationToPdb(REGULATION_TO_SEND, newRegulation);

		// Verify
		if (successfulRequest)
		{
			assertTrue("The client did not correctly interpret the returned JSON",
					EqualsBuilder.reflectionEquals(REGULATION_RETURNED_FROM_PDB, regulationReturnedFromClient));
		}
	}

	private void stubResponseFromPdb(boolean newRegulation)
	{
		stubResponseFromPdb(newRegulation, true);
	}

	private void stubResponseFromPdb(boolean newRegulation, boolean successfulRequest)
	{
		String endpoint = determineEndpoint(REGULATION_TO_SEND, newRegulation);
		String httpMethod = determineHttpMethod(newRegulation);
		if (successfulRequest)
		{
			stub(httpMethod, endpoint, REGULATION_RETURNED_FROM_PDB);
		}
		else
		{
			stub(httpMethod, endpoint, "", Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Determines what HTTP method should be used when sending a regulation to another module.
	 * 
	 * @param newRegulation
	 *        whether the regulation is new.
	 * @return
	 */
	private String determineHttpMethod(boolean newRegulation)
	{
		String httpMethod = HttpMethod.POST;
		if (!newRegulation)
		{
			httpMethod = HttpMethod.PUT;
		}
		return httpMethod;
	}

	/**
	 * Determines what endpoint the client should use when sending a regulation.
	 * 
	 * @param module
	 *        the module to send the regulation to.
	 * @param regulation
	 *        the regulation to send.
	 * @param newRegulation
	 *        whether the regulation is new.
	 * @return
	 */
	private String determineEndpoint(PrivacyRegulation regulation, boolean newRegulation)
	{
		String endpoint = ENDPOINT_POLICY_DB_REGULATIONS;
		if (!newRegulation)
		{
			String regulationId = regulation.getRegId();
			endpoint = ENDPOINT_POLICY_DB_REGULATIONS_VARIABLE_REG_ID.replace("{reg_id}", regulationId);
		}
		return endpoint;
	}

	/**
	 * Ask the client to send a regulation to the PDB.
	 * 
	 * @param regulation
	 *        the regulation to send.
	 * @param newRegulation
	 *        whether the regulation is new.
	 * @return the regulation the PDB returns.
	 * @throws HttpException
	 */
	private PrivacyRegulation sendRegulationToPdb(PrivacyRegulation regulation, boolean newRegulation) throws OperandoCommunicationException
	{
		PrivacyRegulation regulationFromPdb = null;

		if (newRegulation)
		{
			regulationFromPdb = client.createNewRegulationOnPolicyDb(regulation.getInputObject());
		}
		else
		{
			client.updateExistingRegulationOnPolicyDb(regulation.getRegId(), regulation.getInputObject());
		}

		return regulationFromPdb;
	}
}
