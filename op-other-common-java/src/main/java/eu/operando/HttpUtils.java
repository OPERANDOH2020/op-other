package eu.operando;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;

public class HttpUtils
{
	/**
	 * Checks to see if a status code is in a particular family.
	 */
	public static boolean statusCodeIsInFamily(int statusCode, Family family)
	{
		Status status = Status.fromStatusCode(statusCode);
		Family statusFamilyResponse = status.getFamily();
		boolean doesFamilyContainStatus = statusFamilyResponse.equals(family);
		return doesFamilyContainStatus;
	}
	
	public static boolean requestSuccessful(Response response)
	{
		return statusCodeIsInFamily(response.getStatus(), Status.Family.SUCCESSFUL);
	}
}
