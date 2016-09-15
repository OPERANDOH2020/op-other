package eu.operando.moduleclients;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.NotImplementedException;

public abstract class ClientOperandoModule
{
	private static final String PATH_OPERANDO = "/operando";
	protected static final String PATH_OPERANDO_CORE = PATH_OPERANDO + "/core";
	protected static final String PATH_OPERANDO_INTERFACES = PATH_OPERANDO + "/interfaces";
	protected static final String PATH_OPERANDO_PDR = PATH_OPERANDO + "/pdr";
	protected static final String PATH_OPERANDO_WEBUI = PATH_OPERANDO + "/webui";
	
	private Client client = ClientBuilder.newClient();
	private String originOfTarget = "";

	protected ClientOperandoModule(String originOfTarget)
	{
		this.originOfTarget = originOfTarget;
	}

	protected Response sendRequest(String httpMethod, String endpoint)
	{
		return sendRequest(httpMethod, endpoint, null);
	}

	protected Response sendRequestWithQueryParameters(String httpMethod, String endpoint, MultivaluedMap<String, String> queryParams)
	{
		return sendRequest(httpMethod, endpoint, null, queryParams);
	}

	protected Response sendRequest(String httpMethod, String endpoint, Object objectInBodyAsJson)
	{
		return sendRequest(httpMethod, endpoint, objectInBodyAsJson, new MultivaluedStringMap());
	}

	protected Response sendRequest(String httpMethod, String endpoint, Object objectInBodyAsJson, MultivaluedMap<String, String> queryParams)
	{
		Response response;

		// Create a web target for the correct URL.
		WebTarget target = client.target(originOfTarget);
		target = target.path(endpoint);
		target = addQueryParameters(target, queryParams);

		// Send the request.
		Builder requestBuilder = target.request();
		switch (httpMethod)
		{
			case HttpMethod.GET:
				response = requestBuilder.get();
				break;
			case HttpMethod.POST:
				if (objectInBodyAsJson != null)
				{
					response = requestBuilder.post(createEntityStringJson(objectInBodyAsJson));
				}
				else
				{
					// This is a pretty horrible workaround to post with an empty body. See https://java.net/jira/browse/JERSEY-2370.
					response = requestBuilder.post(Entity.entity(null, "foo/bar"));					
				}
				break;
			case HttpMethod.PUT:
				response = requestBuilder.put(createEntityStringJson(objectInBodyAsJson));
				break;
			default:
				throw new NotImplementedException("eu.operando.moduleclients.ClientOp.sendRequest has not been implemented for the HTTP method " + httpMethod);
		}

		return response;
	}

	/**
	 * Add query parameters to a web target.
	 * 
	 * @param target
	 *        the web target to add the query parameters to.
	 * @param parametersOptional
	 *        a map from parameter names to the values against that name.
	 * @return
	 */
	private WebTarget addQueryParameters(WebTarget target, MultivaluedMap<String, String> parametersOptional)
	{
		// Iterate over the set of parameter names.
		Set<String> parameterNames = parametersOptional.keySet();
		Iterator<String> iteratorParameterNames = parameterNames.iterator();
		while (iteratorParameterNames.hasNext())
		{
			// Iterate over the list of values corresponding to a particular parameter name.
			String parameterName = iteratorParameterNames.next();
			List<String> parameterValues = parametersOptional.get(parameterName);
			Iterator<String> iteratorParameterValues = parameterValues.iterator();
			while (iteratorParameterValues.hasNext())
			{
				// Add each parameter value as a query parameter against the parameter name.
				String parameterValue = iteratorParameterValues.next();
				target = target.queryParam(parameterName, parameterValue);
			}
		}

		return target;
	}

	/**
	 * Convert JSON (using OPERANDO's default JSON format) to a POJO.
	 */
	protected static <T> T createObjectFromJsonFollowingOperandoConventions(String strJson, Class<T> classOfT)
	{
		Gson gson = getGsonOperando();
		return gson.fromJson(strJson, classOfT);
	}

	/**
	 * Convert JSON (using OPERANDO's default JSON format) to a POJO.
	 */
	protected static <T> T createObjectFromJsonFollowingOperandoConventions(String strJson, Type typeOfT)
	{
		Gson gson = getGsonOperando();
		return gson.fromJson(strJson, typeOfT);
	}

	/**
	 * Takes in a java object, converts it to JSON, and returns an entity containing the JSON string.
	 */
	private static <T> Entity<String> createEntityStringJson(T object)
	{
		String json = createStringJsonFollowingOperandoConventions(object);
		return Entity.entity(json, MediaType.APPLICATION_JSON);
	}

	/**
	 * Convert a POJO to JSON using OPERANDO's default JSON format
	 */
	private static String createStringJsonFollowingOperandoConventions(Object object)
	{
		Gson gson = getGsonOperando();
		return gson.toJson(object);
	}

	/**
	 * Returns a Gson with OPERANDO's field naming policy.
	 */
	private static Gson getGsonOperando()
	{
		// According to our current conventions, JSON should be in snake_case.
		GsonBuilder builder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		Gson gson = builder.create();
		return gson;
	}
}
