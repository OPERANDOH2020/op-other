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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This abstract class represents a 'client' internal to an OPERANDO module, whose purpose is
 * to process requests from the 'internal' components of the module which require interaction
 * with 'servers' outside the module (e.g. other OPERANDO modules).
 */
@SuppressWarnings("unused")
public abstract class ClientOperandoModule
{
	//Constants defined in https://docs.google.com/spreadsheets/d/1ZBKxcjAeaQXDg4tWkGII4njJYhZKAYRg8ULOvcu0M3o/edit#gid=0
	//Module ID to represent an OPERANDO module in intra-platform communication
	public static final int MODULE_ID_ANONYMIZATION_ENGINE = 1001;
	public static final int MODULE_ID_AUTHENTICATION_SERVICE = 1002;
	public static final int MODULE_ID_BIG_DATA_ANALYTICS = 1003;
	public static final int MODULE_ID_OSP_ENFORCEMENT = 1004;
	public static final int MODULE_ID_PRIVACY_FOR_BENEFIT = 1005;
	public static final int MODULE_ID_POLICY_COMPUTATION = 1006;
	public static final int MODULE_ID_RIGHTS_MANAGEMENT = 1008;
	public static final int MODULE_ID_USER_DEVICE_ENFORCEMENT = 1009;
	public static final int MODULE_ID_PRIVACY_WATCHDOG = 1010;
	public static final int MODULE_ID_POLICIES_DB = 1011;
	public static final int MODULE_ID_USER_ACCOUNTS_DB = 1012;
	public static final int MODULE_ID_LOG_DB = 1013;
	public static final int MODULE_ID_EMAIL_SERVICES = 2001;
	public static final int MODULE_ID_OSP_API = 2002;
	public static final int MODULE_ID_REGULATOR_API = 2003;
	public static final int MODULE_ID_USER_AGENT_MIDDLEWARE = 2004;
	public static final int MODULE_ID_WEB_SERVICES = 2005;
	public static final int MODULE_ID_ADMINISTRATION_CONSOLE = 3001;
	public static final int MODULE_ID_MANAGEMENT_CONSOLE = 3002;
	public static final int MODULE_ID_REPORT_GENERATOR = 3002;
	public static final int MODULE_ID_GATEKEEPER = 4001;
	public static final int MODULE_ID_DATA_ACCESS_NODE = 4002;
	public static final int MODULE_ID_REPOSITORY_MANAGER = 4003;
	public static final int MODULE_ID_USER_DATA_REPOSITORY = 4004;

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
	public static final String PATH_EXTERNAL_OPERANDO_AUTHENTICATION_API = "/authentication";
	public static final String PATH_EXTERNAL_OPERANDO_GATEKEEPER = "/gatekeeper";
	
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
	public static final String ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION = PATH_EXTERNAL_OPERANDO_AUTHENTICATION_API + "/tickets/service_ticket/%s/validation"; 
	
	private Client client = ClientBuilder.newClient();

	public Client getClient()
	{
		return client;
	}
	
	/**
	 * Convert a POJO to JSON using OPERANDO's default JSON format
	 */
	public static String createStringJsonFollowingOperandoConventions(Object object)
	{
		Gson gson = getGsonOperando();
		return gson.toJson(object);
	}

	/**
	 * Convert JSON (using OPERANDO's default JSON format) to a POJO.
	 */
	public static <T> T getObjectFromJsonFollowingOperandoConventions(String strJson, Class<T> classOfT)
	{
		Gson gson = getGsonOperando();
		return gson.fromJson(strJson, classOfT);
	}

	/**
	 * Convert JSON (using OPERANDO's default JSON format) to a POJO.
	 */
	public static <T> T getObjectFromJsonFollowingOperandoConventions(String strJson, Type typeOfT)
	{
		Gson gson = getGsonOperando();
		return gson.fromJson(strJson, typeOfT);
	}

	/**
	 * Returns a Gson with OPERANDO's field naming policy.
	 */
	private static Gson getGsonOperando()
	{
		//According to our current conventions, JSON should be in snake_case.
		GsonBuilder builder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		Gson gson = builder.create();
		return gson;
	}
	
	/**
	 * Takes in a java object, converts it to JSON, and returns an entity containing the JSON string.
	 */
	public static <T> Entity<String> createEntityStringJsonFromObject(T object)
	{
		String json = createStringJsonFollowingOperandoConventions(object);
		return Entity.entity(json, MediaType.APPLICATION_JSON);
	}
}
