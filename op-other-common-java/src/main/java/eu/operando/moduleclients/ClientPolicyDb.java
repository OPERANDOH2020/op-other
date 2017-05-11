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
	private static final String ENDPOINT_POLICY_DB_REGULATIONS_VARIABLE_REG_ID = ENDPOINT_POLICY_DB_REGULATIONS + "/{reg_id}";

	public ClientPolicyDb(String originPolicyDb)
	{
		super(originPolicyDb);
	}
	
	public PrivacyPolicy getPrivacyPolicyForOsp(String ospId) throws OperandoCommunicationException
	{
		String endpoint = ENDPOINT_POLICY_DB_OSP_VARIABLE_OSP_ID_PRIVACY_POLICY.replace("{osp_id}", ospId);
		return sendRequestToPdb(HttpMethod.GET, null, endpoint, PrivacyPolicy.class);
	}

	public PrivacyRegulation createNewRegulationOnPolicyDb(PrivacyRegulationInput privacyReulationInput) throws OperandoCommunicationException
	{
		return sendRequestToPdb(HttpMethod.POST, privacyReulationInput, ENDPOINT_POLICY_DB_REGULATIONS, PrivacyRegulation.class);
	}

	public PrivacyRegulation updateExistingRegulationOnPolicyDb(String regId, PrivacyRegulationInput input) throws OperandoCommunicationException
	{
		String endpoint = ENDPOINT_POLICY_DB_REGULATIONS_VARIABLE_REG_ID.replace("{reg_id}", regId);
		return sendRequestToPdb(HttpMethod.PUT, input, endpoint, PrivacyRegulation.class);
	}

	/**
	 * Encodes an object in JSON and sends it to the specified endpoint of the PDB, using the specified HTTP method. If possible, a
	 * object of the specified type is read from the response. If not, an HttpException is thrown.
	 * 
	 * @param httpMethod
	 *        the HTTP method to send in the request.
	 * @param content
	 *        the object to send to the PDB.
	 * @param endpoint
	 *        the endpoint of the PDB to send the request to.
	 * @param responseClass
	 * 		  the expected type of the response
	 * 
	 * @return the object in the response from the PDB as the specified type.
	 * @throws HttpException
	 *         if the request is not successful
	 */	
	private <T> T sendRequestToPdb(String httpMethod, Object content, String endpoint, Class<T> responseClass) throws OperandoCommunicationException
	{
		Response response = sendRequest(httpMethod, endpoint, content);

		// Only try to interpret the response if the status code is a success code.
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

		String strJson = response.readEntity(String.class);
		return createObjectFromJsonFollowingOperandoConventions(strJson, responseClass);
	}
}
