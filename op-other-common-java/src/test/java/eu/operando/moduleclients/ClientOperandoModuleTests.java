package eu.operando.moduleclients;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.NotImplementedException;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Ignore;
import org.junit.Rule;

import com.github.tomakehurst.wiremock.client.RemoteMappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Ignore // This class contains helper methods for tests, not tests itself. TODO - make a helper class rather than superclass?
public class ClientOperandoModuleTests
{
	private static final String PREFIX_HTTP = "http://";
	private static final int PORT_WIREMOCK = 8089;
	private static final String HOST_WIREMOCK = "localhost:" + PORT_WIREMOCK;
	protected static final String ORIGIN_WIREMOCK = PREFIX_HTTP + HOST_WIREMOCK;

	@Rule
	// Must be public to be annotated as a rule.
	public WireMockRule wireMockRule = new WireMockRule(PORT_WIREMOCK);

	/**
	 * Stubbing
	 */
	/**
	 * httpMethod must be one of GET/POST/PUT.
	 */
	public void stub(String httpMethod, String endpoint)
	{
		stub(httpMethod, endpoint, null);
	}

	/**
	 * httpMethod must be one of GET/POST/PUT.
	 */
	public void stub(String httpMethod, String endpoint, Object objectInResponseBody)
	{
		stub(httpMethod, endpoint, objectInResponseBody, null);
	}

	/**
	 * httpMethod must be one of GET/POST/PUT.
	 */
	public void stub(String httpMethod, String endpoint, Object objectInResponseBody, Status status)
	{
		String strJsonBody = "";
		if (objectInResponseBody != null)
		{
			strJsonBody = createStringJsonFollowingOperandoConventions(objectInResponseBody);
		}
		stub(httpMethod, endpoint, strJsonBody, status);
	}

	public void stub(String httpMethod, String endpoint, String strBody, Status status)
	{
		// Check validity of parameters.
		checkValidHttpMethod(httpMethod);

		// Build up the expected request.
		@SuppressWarnings("rawtypes")
		RemoteMappingBuilder mappingBuilder = get(urlPathEqualTo(endpoint));
		if (httpMethod.equals(HttpMethod.POST))
		{
			mappingBuilder = post(urlPathEqualTo(endpoint));
		}
		else if (httpMethod.equals(HttpMethod.PUT))
		{
			mappingBuilder = put(urlPathEqualTo(endpoint));
		}

		// Build up the stub response.
		ResponseDefinitionBuilder response = aResponse();
		if (!strBody.isEmpty())
		{
			response.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
			response.withBody(strBody);
		}
		if (status != null)
		{
			int statusCode = status.getStatusCode();
			response.withStatus(statusCode);
		}

		// Make sure that if a request is received matching the expected request, then the correct stub response is returned.
		mappingBuilder.willReturn(response);
		wireMockRule.stubFor(mappingBuilder);
	}

	/**
	 * Verification.
	 */
	public void verifyCorrectHttpRequest(String httpMethod, String endpoint)
	{
		verifyCorrectHttpRequest(httpMethod, endpoint, null);
	}

	public void verifyCorrectHttpRequest(String httpMethod, String endpoint, Object objectInBody)
	{
		verifyCorrectHttpRequest(httpMethod, endpoint, new MultivaluedStringMap(), new MultivaluedStringMap(), objectInBody, false);
	}

	public void verifyCorrectHttpRequestWithQueryParams(String httpMethod, String endpoint, MultivaluedMap<String, String> queryParametersExpected)
	{
		verifyCorrectHttpRequest(httpMethod, endpoint, new MultivaluedStringMap(), queryParametersExpected, null, false);
	}

	public void verifyCorrectHttpRequest(String httpMethod, String endpoint, MultivaluedMap<String, String> headersExpected, MultivaluedMap<String, String> queryParametersExpected, Object objectInBody)
	{
		verifyCorrectHttpRequest(httpMethod, endpoint, headersExpected, queryParametersExpected, objectInBody, false);
	}

	public void verifyCorrectHttpRequest(String httpMethod, String endpoint, MultivaluedMap<String, String> headersExpected, MultivaluedMap<String, String> queryParametersExpected, Object objectInBody, boolean alreadyJsonEncoded)
	{
		checkValidHttpMethod(httpMethod);

		// correct verb and endpoint
		RequestPatternBuilder requestPatternBuilder = getRequestedFor(urlPathEqualTo(endpoint));
		if (httpMethod.equals(HttpMethod.POST))
		{
			requestPatternBuilder = postRequestedFor(urlPathEqualTo(endpoint));
		}
		else if (httpMethod.equals(HttpMethod.PUT))
		{
			requestPatternBuilder = putRequestedFor(urlPathEqualTo(endpoint));
		}
		else if (httpMethod.equals(HttpMethod.DELETE))
		{
			requestPatternBuilder = deleteRequestedFor(urlPathEqualTo(endpoint));
		}

		// correct headers
		addHeadersOrQueryParameters(requestPatternBuilder, headersExpected, true);
		
		// correct queries
		addHeadersOrQueryParameters(requestPatternBuilder, queryParametersExpected, false);

		// correct body; sometimes want an empty body.
		if (objectInBody != null)
		{
			String stringJson = "";
			if (alreadyJsonEncoded)
			{
				stringJson = (String) objectInBody;
			}
			else
			{
				stringJson = createStringJsonFollowingOperandoConventions(objectInBody);
			}
			
			requestPatternBuilder.withRequestBody(equalToJson(stringJson));
		}

		// verify
		wireMockRule.verify(requestPatternBuilder);
	}

	/**
	 * Add headers or query params to the expected request pattern.
	 * @param areHeaders
	 * 	true if adding headers, false if adding query params.
	 */
	private void addHeadersOrQueryParameters(RequestPatternBuilder requestPatternBuilder, MultivaluedMap<String, String> expected, boolean areHeaders)
	{
		Set<String> keySet = expected.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext())
		{
			String key = iterator.next();
			List<String> values = expected.get(key);
			for (int i = 0; i < values.size(); i++)
			{
				String value = values.get(i);
				StringValuePattern equalToValue = equalTo(value);
				if (areHeaders)
				{
					requestPatternBuilder.withHeader(key, equalToValue);
				}
				else
				{
					requestPatternBuilder.withQueryParam(key, equalToValue);
				}
			}
		}
	}

	/**
	 * Make sure that the method that was passed in is a supported method.
	 */
	private void checkValidHttpMethod(String httpMethod)
	{
		if (!httpMethod.equals(HttpMethod.GET) && !httpMethod.equals(HttpMethod.POST) && !httpMethod.equals(HttpMethod.PUT) && !httpMethod.equals(HttpMethod.DELETE))
		{
			throw new NotImplementedException("eu.operando.moduleclients.ClientOperandoModuleTests.verifyCorrectHttpRequest has not been implemented for the HTTP method " + httpMethod);
		}
	}

	/**
	 * Convert a POJO to JSON using OPERANDO's default JSON format
	 */
	private String createStringJsonFollowingOperandoConventions(Object object)
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
