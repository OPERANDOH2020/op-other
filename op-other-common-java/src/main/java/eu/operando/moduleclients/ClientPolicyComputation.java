package eu.operando.moduleclients;

import java.util.Vector;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import eu.operando.api.model.PrivacyRegulation;
import eu.operando.api.model.PrivacyRegulationInput;

public class ClientPolicyComputation extends ClientOperandoModule
{
	private static final String ENDPOINT_POLICY_COMPUTATION_REGULATIONS_VARIABLE_REG_ID = "/regulations/{reg_id}";
	
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
		String regId = regulation.getRegId();
		String endpoint = ENDPOINT_POLICY_COMPUTATION_REGULATIONS_VARIABLE_REG_ID.replace("{reg_id}", regId);
		Response responseFromPc = sendRequest(HttpMethod.POST, endpoint, regulation.getInputObject());
		
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
		Vector<PrivacyRegulationInput> regulationVector = new Vector<PrivacyRegulationInput>();
		regulationVector.add(regulation.getInputObject());
		Response responseFromPc = sendRequest(HttpMethod.PUT, endpoint, regulationVector);
		
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
		boolean requestWasSuccessful = statusCodeFromPc == Status.OK.getStatusCode();
		return requestWasSuccessful;
	}
}
