/*
 * Copyright (c) 2016 Oxford Computer Consultants Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT).
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *
 * Contributors:
 *    Matthew Gallagher (Oxford Computer Consultants) - Creation.
 * Initially developed in the context of OPERANDO EU project www.operando.eu
 */
package eu.operando;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.junit.Rule;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class which tests for OPERANDO module clients should extend. Contains the constants that the client should use.
 * There is duplication of code between this test class and the abstract OperandoModuleClient class; I think
 * that is better to have duplicated code for testing, which actually tests what it should, than avoiding code
 * duplication in test code (e.g. by making this class extend OperandoModuleClient) but risk bugs as a result. 
 */
@SuppressWarnings("unused")
public abstract class ClientOperandoModuleTests
{
	public static final String PREFIX_HTTP = "http://";
	public static final int PORT_WIREMOCK = 8089;
	public static final String HOST_WIREMOCK = "localhost:" + PORT_WIREMOCK;
	public static final String ORIGIN_WIREMOCK = PREFIX_HTTP + HOST_WIREMOCK;

	//The base path at which the relevant API can be found
	private static final String PATH_INTERNAL_OPERANDO_CORE_ANONYMIZATION = "/operando/core/anonymization";
	private static final String PATH_INTERNAL_OPERANDO_CORE_AUTHENTICATION = "/operando/core/authentication";
	private static final String PATH_INTERNAL_OPERANDO_CORE_BIGDATA = "/operando/core/bigdata";
	private static final String PATH_INTERNAL_OPERANDO_CORE_OSP_ENFORCEMENT = "/operando/core/osp_enforcement";
	private static final String PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT = "/operando/core/privacy_for_benefit";
	private static final String PATH_INTERNAL_OPERANDO_CORE_POLICY_COMPUTATION = "/operando/core/policy_computation";
	private static final String PATH_INTERNAL_OPERANDO_CORE_RIGHTS_MANAGEMENT = "/operando/core/rights_mng";
	private static final String PATH_INTERNAL_OPERANDO_CORE_DEVICE_ENFORCEMENT = "/operando/core/device_enforcement";
	private static final String PATH_INTERNAL_OPERANDO_CORE_WATCHDOG = "/operando/core/watchdog";
	private static final String PATH_INTERNAL_OPERANDO_CORE_POLICIES_DB = "/operando/core/policies_db";
	private static final String PATH_INTERNAL_OPERANDO_CORE_USER_DB = "/operando/core/user_db";
	private static final String PATH_INTERNAL_OPERANDO_CORE_LOG_DB = "/operando/core/log_db";
	private static final String PATH_INTERNAL_OPERANDO_INTERFACES_EMAIL_SERVICES = "/operando/interfaces/email_services";
	private static final String PATH_INTERNAL_OPERANDO_INTERFACES_OSP_API = "/operando/interfaces/osp_api";
	private static final String PATH_INTERNAL_OPERANDO_INTERFACES_REGULATOR = "/operando/interfaces/regulator";
	private static final String PATH_INTERNAL_OPERANDO_INTERFACES_USER_AGENT = "/operando/interfaces/user_agent";
	private static final String PATH_INTERNAL_OPERANDO_INTERFACES_WEB_SERVICES = "/operando/interfaces/web_services";
	private static final String PATH_INTERNAL_OPERANDO_WEBUI_ADMIN = "/operando/webui/admin";
	private static final String PATH_INTERNAL_OPERANDO_WEBUI_DASHBOARD = "/operando/webui/dashboard";
	private static final String PATH_INTERNAL_OPERANDO_WEBUI_REPORTS = "/operando/webui/reports";
	private static final String PATH_INTERNAL_OPERANDO_PDR_GATEKEEPER = "/operando/pdr/gatekeeper";
	private static final String PATH_INTERNAL_OPERANDO_PDR_DATA_ACCESS_NODE = "/operando/pdr/access_node";
	private static final String PATH_INTERNAL_OPERANDO_PDR_REPOSITORY = "/operando/pdr/repository";
	
	//External base paths.
	private static final String PATH_EXTERNAL_OPERANDO_AUTHENTICATION_API = "/authentication"; //TODO - waiting on UPRC to change to match.

	private static final String URL_AUTHENTICATION_SERVICE = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_CORE_AUTHENTICATION;
	private static final String URL_RIGHTS_MANAGEMENT = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_CORE_RIGHTS_MANAGEMENT;
	private static final String URL_DATA_ACCESS_NODE = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_PDR_DATA_ACCESS_NODE;
	private static final String URL_LOG_DB = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_CORE_LOG_DB;
	private static final String URL_REPORT_GENERATOR = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_WEBUI_REPORTS;
	private static final String URL_POLICY_COMPUTATION = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_CORE_POLICY_COMPUTATION;
	private static final String URL_OSP_ENFORCEMENT = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_CORE_OSP_ENFORCEMENT;
	private static final String URL_USER_DEVICE_ENFORCEMENT = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_CORE_DEVICE_ENFORCEMENT;
	private static final String URL_EMAIL_SERVICES = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_INTERFACES_EMAIL_SERVICES;
	private static final String URL_POLICY_DB = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_CORE_POLICIES_DB;
	private static final String URL_BIG_DATA_ANALYTICS = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_CORE_BIGDATA;
	private static final String URL_PRIVACY_FOR_BENEFIT = ORIGIN_WIREMOCK + PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT;

	//RESTful endpoints for various modules
	public static final String ENDPOINT_POLICY_COMPUTATION_REGULATIONS_VARIABLE_REG_ID = PATH_INTERNAL_OPERANDO_CORE_POLICY_COMPUTATION + "/regulations/%d";
	public static final String ENDPOINT_OSP_ENFORCEMENT_REGULATIONS_VARIABLE_REG_ID = PATH_INTERNAL_OPERANDO_CORE_OSP_ENFORCEMENT + "/regulations/%d";
	public static final String ENDPOINT_OSP_ENFORCEMENT_PRIVACY_SETTINGS_VARIABLE_OSP_ID = PATH_INTERNAL_OPERANDO_CORE_OSP_ENFORCEMENT + "/osps/%d/privacy_settings";
	public static final String ENDPOINT_USER_DEVICE_ENFORCEMENT_PRIVACY_SETTINGS = PATH_INTERNAL_OPERANDO_CORE_DEVICE_ENFORCEMENT + "/privacy_settings";
	public static final String ENDPOINT_EMAIL_SERVICES_EMAIL_NOTIFICATION = PATH_INTERNAL_OPERANDO_INTERFACES_EMAIL_SERVICES + "/email_notification";
	public static final String ENDPOINT_POLICY_DB_REGULATIONS = PATH_INTERNAL_OPERANDO_CORE_POLICIES_DB + "/regulations";
	public static final String ENDPOINT_POLICY_DB_REGULATIONS_VARIABLE_REG_ID = ENDPOINT_POLICY_DB_REGULATIONS + "/%d";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID = PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/deals/%d";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID_ACKNOWLEDGEMENT = ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID + "/acknowledgement";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS = PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/offers";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS_VARIABLE_OFFER_ID = ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS + "/%d";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OSPS_VARIABLE_OSP_ID_DEALS = PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/osps/%d/deals";
	public static final String ENDPOINT_BIG_DATA_ANALYTICS_REPORTS_VARIABLE_REPORT_ID = PATH_INTERNAL_OPERANDO_CORE_BIGDATA + "/reports/%d";
	public static final String ENDPOINT_REPORT_GENERATOR_REPORTS = PATH_INTERNAL_OPERANDO_WEBUI_REPORTS + "/reports";
	public static final String ENDPOINT_LOG_DB_LOG = PATH_INTERNAL_OPERANDO_CORE_LOG_DB + "/log";
	public static final String ENDPOINT_WEB_SERVICES_PRIVACY_POLICIES = PATH_INTERNAL_OPERANDO_INTERFACES_WEB_SERVICES + "/GetOSPPrivacyTerms";
	public static final String ENDPOINT_WEB_SERVICES_PRIVACY_OPTIONS = PATH_INTERNAL_OPERANDO_INTERFACES_WEB_SERVICES + "/GetOSPSettings";
	public static final String ENDPOINT_RIGHTS_MANAGEMENT_QUERY_EVALUATOR = PATH_INTERNAL_OPERANDO_CORE_RIGHTS_MANAGEMENT + "/QueryEvaluator";
	public static final String ENDPOINT_DATA_ACCESS_NODE_DAN_URL_FOR_QUERY = PATH_INTERNAL_OPERANDO_PDR_DATA_ACCESS_NODE + "/dan_url_for_query";
	
	//External endpoints for various modules.
	public static final String ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION = PATH_EXTERNAL_OPERANDO_AUTHENTICATION_API + "/tickets/service_ticket/%s/validation"; //TODO - this is liable to change and should be checked. 

	@Rule
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
		stub(httpMethod, endpoint, objectInResponseBody, -1);
	}
	/**
	 * httpMethod must be one of GET/POST/PUT.
	 */
	public void stub(String httpMethod, String endpoint, Object objectInResponseBody, int statusCode)
	{
		String strJsonBody = "";
		if (objectInResponseBody != null)
		{
			strJsonBody = createStringJsonFollowingOperandoConventions(objectInResponseBody);
		}
		stub(httpMethod, endpoint, strJsonBody, statusCode);
	}
	public void stub(String httpMethod, String endpoint, String strJsonBody, int statusCode)
	{
		//Check validity of parameters.
		checkValidHttpMethod(httpMethod);
		
		//Build up the expected request.
		MappingBuilder mappingBuilder = get(urlPathEqualTo(endpoint));
		if (httpMethod.equals(HttpMethod.POST))
		{
			mappingBuilder = post(urlPathEqualTo(endpoint));
		}
		else if (httpMethod.equals(HttpMethod.PUT))
		{
			mappingBuilder = put(urlPathEqualTo(endpoint));
		}
		
		//Build up the stub response.
		ResponseDefinitionBuilder response = aResponse();
		if (!strJsonBody.isEmpty())
		{
			response.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
			response.withBody(strJsonBody);
		}
		if (statusCode > -1)
		{
			response.withStatus(statusCode);
		}
		
		//Make sure that if a request is received matching the expected request, then the correct stub response is returned. 
		mappingBuilder.willReturn(response);		
		wireMockRule.stubFor(mappingBuilder);
	}
	
	/**
	 * Verification.
	 */
	public void verifyCorrectHttpRequest(String httpMethod, String endpoint)
	{
		verifyCorrectHttpRequestWithoutQueryParams(httpMethod, endpoint, null);
	}
	public void verifyCorrectHttpRequestWithoutQueryParams(String httpMethod, String endpoint, Object objectInBodyAsJson)
	{
		verifyCorrectHttpRequest(httpMethod, endpoint, new HashMap<String, String>(), objectInBodyAsJson);
	}
	public void verifyCorrectHttpRequestWithoutBody(String httpMethod, String endpoint, HashMap<String, String> queriesParamToValue)
	{
		verifyCorrectHttpRequest(httpMethod, endpoint, queriesParamToValue, null);
	}
	public void verifyCorrectHttpRequest(String httpMethod, String endpoint, HashMap<String, String> queriesParamToValue, Object objectInBodyAsJson)
	{
		checkValidHttpMethod(httpMethod);
		
		//correct verb and endpoint
		RequestPatternBuilder requestPatternBuilder = getRequestedFor(urlPathEqualTo(endpoint));
		if (httpMethod.equals(HttpMethod.POST))
		{
			requestPatternBuilder = postRequestedFor(urlPathEqualTo(endpoint));
		}
		else if (httpMethod.equals(HttpMethod.PUT))
		{
			requestPatternBuilder = putRequestedFor(urlPathEqualTo(endpoint));
		}

		//correct queries 
		addQueries(queriesParamToValue, requestPatternBuilder);

		//correct body; sometimes want an empty body.
		if (objectInBodyAsJson != null)
		{
			String stringJson = createStringJsonFollowingOperandoConventions(objectInBodyAsJson);
			requestPatternBuilder.withRequestBody(equalToJson(stringJson));
		}

		//verify
		wireMockRule.verify(requestPatternBuilder);
	}
	
	/**
	 * Add queries from hashmap.
	 */
	private void addQueries(HashMap<String, String> queriesParamToValue, RequestPatternBuilder requestPatternBuilder)
	{
		Set<String> keySet = queriesParamToValue.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext())
		{
			String key = iterator.next();
			String value = queriesParamToValue.get(key);
			requestPatternBuilder.withQueryParam(key, equalTo(value));
		}
	}
	
	/**
	 * Make sure that the method that was passed in is a supported method.
	 */
	private void checkValidHttpMethod(String httpMethod)
	{
		if (!httpMethod.equals(HttpMethod.GET)
		  && !httpMethod.equals(HttpMethod.POST)
		  && !httpMethod.equals(HttpMethod.PUT))
		{
			throw new IllegalArgumentException("invalid HTTP verb");
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
	private Gson getGsonOperando()
	{
		//According to our current conventions, JSON should be in snake_case.
		GsonBuilder builder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		Gson gson = builder.create();
		return gson;
	}
}
