package eu.operando.moduleclients;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import eu.operando.HttpUtils;
import eu.operando.OperandoCommunicationException;
import eu.operando.OperandoCommunicationException.CommunicationError;
import eu.operando.api.model.PrivacyPolicy;
import eu.operando.api.model.PrivacyRegulation;
import eu.operando.api.model.PrivacyRegulationInput;

public class ClientPolicyDb extends ClientOperandoModule
{
	private static final String ENDPOINT_POLICY_DB_OSP_VARIABLE_OSP_ID_PRIVACY_POLICY = "/OSP/{osp_id}/privacy-policy";
	private static final String ENDPOINT_POLICY_DB_REGULATIONS = "/regulations";
	private static final String REG_ID = "{reg_id}";
	private static final String ENDPOINT_POLICY_DB_REGULATIONS_VARIABLE_REG_ID = ENDPOINT_POLICY_DB_REGULATIONS + "/" + REG_ID;

	public ClientPolicyDb(String originPolicyDb)
	{
		super(originPolicyDb);
	}
	
	public PrivacyPolicy getPrivacyPolicyForOsp(String ospId) throws OperandoCommunicationException
	{
		String endpoint = ENDPOINT_POLICY_DB_OSP_VARIABLE_OSP_ID_PRIVACY_POLICY.replace("{osp_id}", ospId);
		Response response = sendRequest(HttpMethod.GET, endpoint);

		validateResponse(response);

		String strJson = response.readEntity(String.class);
		return createObjectFromJsonFollowingOperandoConventions(strJson, PrivacyPolicy.class);
	}

	public PrivacyRegulation createNewRegulationOnPolicyDb(PrivacyRegulationInput privacyReulationInput) throws OperandoCommunicationException
	{
		Response response = sendRequest(HttpMethod.POST, ENDPOINT_POLICY_DB_REGULATIONS, privacyReulationInput);

		validateResponse(response);

		String strJson = response.readEntity(String.class);
		return createObjectFromJsonFollowingOperandoConventions(strJson, PrivacyRegulation.class);
	}

	public void updateExistingRegulationOnPolicyDb(String regId, PrivacyRegulationInput input) throws OperandoCommunicationException
	{
		String endpoint = ENDPOINT_POLICY_DB_REGULATIONS_VARIABLE_REG_ID.replace(REG_ID, regId);
		Response response = sendRequest(HttpMethod.PUT, endpoint, input);

		validateResponse(response);
	}

	public PrivacyRegulation getRegulation(String regId) throws OperandoCommunicationException
	{
		String endpoint = ENDPOINT_POLICY_DB_REGULATIONS_VARIABLE_REG_ID.replace(REG_ID, regId);
		Response response = sendRequest(HttpMethod.GET, endpoint);

		validateResponse(response);

		String strJson = response.readEntity(String.class);
		return createObjectFromJsonFollowingOperandoConventions(strJson, PrivacyRegulation.class);
	}
	
	/**
	 * @throwsOperandoCommunicationExceptionn
	 *         if the request is not successful
	 */	
	private void validateResponse(Response response) throws OperandoCommunicationException
	{
		int statusCodeResponse = response.getStatus();
		boolean responseSuccessful = HttpUtils.statusCodeIsInFamily(statusCodeResponse, Status.Family.SUCCESSFUL);
		if (!responseSuccessful)
		{
			CommunicationError communicationError = CommunicationError.OTHER;
			if (statusCodeResponse == Status.NOT_FOUND.getStatusCode())
			{
				communicationError = CommunicationError.REQUESTED_RESOURCE_NOT_FOUND;
			}
			throw new OperandoCommunicationException(communicationError, "A response from the PDB had status code: " + statusCodeResponse);
		}
	}
}
