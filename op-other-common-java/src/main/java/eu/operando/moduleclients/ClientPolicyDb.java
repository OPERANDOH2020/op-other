package eu.operando.moduleclients;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpException;

import eu.operando.HttpUtils;
import eu.operando.OperandoCommunicationException;
import eu.operando.OperandoCommunicationException.CommunicationError;
import eu.operando.api.model.PrivacyRegulation;
import eu.operando.api.model.PrivacyRegulationInput;

public class ClientPolicyDb extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_POLICIES_DB = PATH_OPERANDO_CORE + "/policies_db";
	private static final String ENDPOINT_POLICY_DB_REGULATIONS = PATH_INTERNAL_OPERANDO_CORE_POLICIES_DB + "/regulations";
	private static final String ENDPOINT_POLICY_DB_REGULATIONS_VARIABLE_REG_ID = ENDPOINT_POLICY_DB_REGULATIONS + "/{reg_id}";

	public ClientPolicyDb(String originPolicyDb)
	{
		super(originPolicyDb);
	}

	public PrivacyRegulation createNewRegulationOnPolicyDb(PrivacyRegulationInput privacyReulationInput) throws OperandoCommunicationException
	{
		return sendRegulationToPdb(HttpMethod.POST, privacyReulationInput, ENDPOINT_POLICY_DB_REGULATIONS);
	}

	public PrivacyRegulation updateExistingRegulationOnPolicyDb(String regId, PrivacyRegulationInput input) throws OperandoCommunicationException
	{
		String endpoint = ENDPOINT_POLICY_DB_REGULATIONS_VARIABLE_REG_ID.replace("{reg_id}", regId);
		return sendRegulationToPdb(HttpMethod.PUT, input, endpoint);
	}

	/**
	 * Encodes a regulation in JSON and sends it to the specified endpoint of the PDB, using the specified HTTP method. If possible, a
	 * PrivacyRegulation is read from the response. If not, an HttpException is thrown.
	 * 
	 * @param httpMethod
	 *        the HTTP method to send in the request.
	 * @param privacyRegulationInput
	 *        the regulation to send to the PDB.
	 * @param endpoint
	 *        the endpoint of the PDB to send the request to.
	 * 
	 * @return the privacy regulation in the response from the PDB.
	 * @throws HttpException
	 *         if the request is not successful
	 */
	private PrivacyRegulation sendRegulationToPdb(String httpMethod, PrivacyRegulationInput privacyRegulationInput, String endpoint) throws OperandoCommunicationException
	{
		Response response = sendRequest(httpMethod, endpoint, privacyRegulationInput);

		// Only try to interpret the response if the status code is a success code.
		int statusCodeResponse = response.getStatus();
		boolean responseSuccessful = HttpUtils.statusCodeIsInFamily(statusCodeResponse, Status.Family.SUCCESSFUL);
		if (!responseSuccessful)
		{
			throw new OperandoCommunicationException(CommunicationError.OTHER, "A response from the PDB had status code: " + statusCodeResponse);
		}

		String strJson = response.readEntity(String.class);
		return createObjectFromJsonFollowingOperandoConventions(strJson, PrivacyRegulation.class);
	}
}
