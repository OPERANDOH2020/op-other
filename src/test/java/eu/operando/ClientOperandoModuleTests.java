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

import java.lang.reflect.Type;

import org.junit.Rule;

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
public abstract class ClientOperandoModuleTests
{
	public static final String PREFIX_HTTP = "http://";
	public static final int PORT_WIREMOCK = 8089;
	public static final String HOST_WIREMOCK = "localhost:" + PORT_WIREMOCK;
	public static final String PROTOCOL_AND_HOST = PREFIX_HTTP + HOST_WIREMOCK;

	//The base path at which the relevant API can be found
	public static final String PATH_INTERNAL_OPERANDO_CORE_ANONYMIZATION = "/operando/core/anonymization";
	public static final String PATH_INTERNAL_OPERANDO_CORE_AUTHENTICATION = "/operando/core/authentication";
	public static final String PATH_INTERNAL_OPERANDO_CORE_BIGDATA = "/operando/core/bigdata";
	public static final String PATH_INTERNAL_OPERANDO_CORE_OSP_ENFORCEMENT = "/operando/core/osp_enforcement";
	public static final String PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT = "/operando/core/privacy_for_benefit";
	public static final String PATH_INTERNAL_OPERANDO_CORE_POLICY_COMPUTATION = "/operando/core/policy_computation";
	public static final String PATH_INTERNAL_OPERANDO_CORE_RIGHTS_MNG = "/operando/core/rights_mng";
	public static final String PATH_INTERNAL_OPERANDO_CORE_DEVICE_ENFORCEMENT = "/operando/core/device_enforcement";
	public static final String PATH_INTERNAL_OPERANDO_CORE_WATCHDOG = "/operando/core/watchdog";
	public static final String PATH_INTERNAL_OPERANDO_CORE_POLICIES_DB = "/operando/core/policies_db";
	public static final String PATH_INTERNAL_OPERANDO_CORE_USER_DB = "/operando/core/user_db";
	public static final String PATH_INTERNAL_OPERANDO_CORE_LOG_DB = "/operando/core/log_db";
	public static final String PATH_INTERNAL_OPERANDO_INTERFACES_EMAIL_SERVICES = "/operando/interfaces/email_services";
	public static final String PATH_INTERNAL_OPERANDO_INTERFACES_OSP_API = "/operando/interfaces/osp_api";
	public static final String PATH_INTERNAL_OPERANDO_INTERFACES_REGULATOR = "/operando/interfaces/regulator";
	public static final String PATH_INTERNAL_OPERANDO_INTERFACES_USER_AGENT = "/operando/interfaces/user_agent";
	public static final String PATH_INTERNAL_OPERANDO_INTERFACES_WEB_SERVICES = "/operando/interfaces/web_services";
	public static final String PATH_INTERNAL_OPERANDO_WEBUI_ADMIN = "/operando/webui/admin";
	public static final String PATH_INTERNAL_OPERANDO_WEBUI_DASHBOARD = "/operando/webui/dashboard";
	public static final String PATH_INTERNAL_OPERANDO_WEBUI_REPORTS = "/operando/webui/reports";
	public static final String PATH_INTERNAL_OPERANDO_PDR_GATEKEEPER = "/operando/pdr/gatekeeper";
	public static final String PATH_INTERNAL_OPERANDO_PDR_ACCESS_NODE = "/operando/pdr/access_node";
	public static final String PATH_INTERNAL_OPERANDO_PDR_REPOSITORY = "/operando/pdr/repository";
	
	//External base paths.
	public static final String PATH_EXTERNAL_OPERANDO_AUTHENTICATION_API = "/authentication"; //TODO - waiting on UPRC to change to match.

	public static final String URL_AUTHENTICATION_SERVICE = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_CORE_AUTHENTICATION;
	public static final String URL_RIGHTS_MANAGEMENT = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_CORE_RIGHTS_MNG;
	public static final String URL_DATA_ACCESS_NODE = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_PDR_ACCESS_NODE;
	public static final String URL_LOG_DB = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_CORE_LOG_DB;
	public static final String URL_REPORT_GENERATOR = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_WEBUI_REPORTS;
	public static final String URL_POLICY_COMPUTATION = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_CORE_POLICY_COMPUTATION;
	public static final String URL_OSP_ENFORCEMENT = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_CORE_OSP_ENFORCEMENT;
	public static final String URL_USER_DEVICE_ENFORCEMENT = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_CORE_DEVICE_ENFORCEMENT;
	public static final String URL_EMAIL_SERVICES = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_INTERFACES_EMAIL_SERVICES;
	public static final String URL_POLICY_DB = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_CORE_POLICIES_DB;
	public static final String URL_BIG_DATA_ANALYTICS = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_CORE_BIGDATA;
	public static final String URL_PRIVACY_FOR_BENEFIT = PROTOCOL_AND_HOST + PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT;

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
	public static final String ENDPOINT_REPORT_GENERATOR_REPORTS = PATH_INTERNAL_OPERANDO_WEBUI_REPORTS + "/reports";
	public static final String ENDPOINT_LOG_DB_LOG = PATH_INTERNAL_OPERANDO_CORE_LOG_DB + "/log";
	
	//External endpoints for various modules.
	public static final String ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION = PATH_EXTERNAL_OPERANDO_AUTHENTICATION_API + "/tickets/service_ticket/%s/validation"; //TODO - this is liable to change and should be checked. 

	private WireMockRule wireMockRule = new WireMockRule(PORT_WIREMOCK);

	@Rule
	public WireMockRule getWireMockRule()
	{
		return wireMockRule;
	}

	/**
	 * Helper to convert a POJO to JSON using OPERANDO's default JSON format
	 */
	public String getStringJsonFollowingOperandoConventions(Object object)
	{
		Gson gson = getGsonOperando();
		return gson.toJson(object);
	}

	/**
	 * Helper to convert JSON (using OPERANDO's default JSON format) to a POJO.
	 */
	public <T> T getStringJsonFollowingOperandoConventions(String strJson, Class<T> classOfT)
	{
		Gson gson = getGsonOperando();
		return gson.fromJson(strJson, classOfT);
	}

	/**
	 * Helper to convert JSON (using OPERANDO's default JSON format) to a POJO.
	 */
	public <T> T getObjectFromJsonFollowingOperandoConventions(String strJson, Type typeOfT)
	{
		Gson gson = getGsonOperando();
		return gson.fromJson(strJson, typeOfT);
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
