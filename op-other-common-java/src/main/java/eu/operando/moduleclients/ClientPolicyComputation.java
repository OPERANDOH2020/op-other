package eu.operando.moduleclients;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import eu.operando.api.model.PrivacyRegulation;

public class ClientPolicyComputation extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_POLICY_COMPUTATION = PATH_OPERANDO_CORE + "/policy_computation";
	private static final String ENDPOINT_POLICY_COMPUTATION_REGULATIONS = PATH_INTERNAL_OPERANDO_CORE_POLICY_COMPUTATION + "/regulations";
	private static final String ENDPOINT_POLICY_COMPUTATION_REGULATIONS_VARIABLE_REG_ID = ENDPOINT_POLICY_COMPUTATION_REGULATIONS + "/{reg_id}";
	
	public ClientPolicyComputation(String originPolicyComputation)
	{
		super(originPolicyComputation);
	}

	/**
	 * Inform PC of a new regulation.
	 * @param regulation
	 * 	The regulation.
	 * @return
	 * 	Whether the request was accepted.
	 */
	public boolean sendNewRegulationToPolicyComputation(PrivacyRegulation regulation)
	{
		Response responseFromPc = sendRequest(HttpMethod.POST, ENDPOINT_POLICY_COMPUTATION_REGULATIONS, regulation.getInputObject());
		
		return wasPolicyComputationRequestSuccessful(responseFromPc);
	}

	/**
	 * Inform PC that a regulation has been updated.
	 * @param regulation
	 * 	The regulation.
	 * @return
	 * 	Whether the request was accepted.
	 */
	public boolean sendExistingRegulationToPolicyComputation(PrivacyRegulation regulation)
	{
		String regId = regulation.getRegId();
		String endpoint = ENDPOINT_POLICY_COMPUTATION_REGULATIONS_VARIABLE_REG_ID.replace("{reg_id}", regId);
		Response responseFromPc = sendRequest(HttpMethod.PUT, endpoint, regulation.getInputObject());
		
		return wasPolicyComputationRequestSuccessful(responseFromPc);
	}

	/**
	 * Analyses a response from Policy Computation to see if it was successful. 
	 * @param responseFromPc
	 * 	the response to be analysed.
	 * @return
	 * 	whether it was successful
	 */
	private boolean wasPolicyComputationRequestSuccessful(Response responseFromPc)
	{
		int statusCodeFromPc = responseFromPc.getStatus();
		boolean requestWasSuccessful = statusCodeFromPc == Status.ACCEPTED.getStatusCode();
		return requestWasSuccessful;
	}
}