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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class OperandoModuleClient
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
	public static final String PATH_OPERANDO_CORE_ANONYMIZATION = "/operando/core/anonymization";
	public static final String PATH_OPERANDO_CORE_AUTHENTICATION = "/operando/core/authentication";
	public static final String PATH_OPERANDO_CORE_BIGDATA = "/operando/core/bigdata";
	public static final String PATH_OPERANDO_CORE_OSP_ENFORCEMENT = "/operando/core/osp_enforcement";
	public static final String PATH_OPERANDO_CORE_PRIVACY_FOR_BENEFIT = "/operando/core/privacy_for_benefit";
	public static final String PATH_OPERANDO_CORE_POLICY_COMPUTATION = "/operando/core/policy_computation";
	public static final String PATH_OPERANDO_CORE_RIGHTS_MNG = "/operando/core/rights_mng";
	public static final String PATH_OPERANDO_CORE_DEVICE_ENFORCEMENT = "/operando/core/device_enforcement";
	public static final String PATH_OPERANDO_CORE_WATCHDOG = "/operando/core/watchdog";
	public static final String PATH_OPERANDO_CORE_POLICIES_DB = "/operando/core/policies_db";
	public static final String PATH_OPERANDO_CORE_USER_DB = "/operando/core/user_db";
	public static final String PATH_OPERANDO_CORE_LOG_DB = "/operando/core/log_db";
	public static final String PATH_OPERANDO_INTERFACES_EMAIL_SERVICES = "/operando/interfaces/email_services";
	public static final String PATH_OPERANDO_INTERFACES_OSP_API = "/operando/interfaces/osp_api";
	public static final String PATH_OPERANDO_INTERFACES_REGULATOR = "/operando/interfaces/regulator";
	public static final String PATH_OPERANDO_INTERFACES_USER_AGENT = "/operando/interfaces/user_agent";
	public static final String PATH_OPERANDO_INTERFACES_WEB_SERVICES = "/operando/interfaces/web_services";
	public static final String PATH_OPERANDO_WEBUI_ADMIN = "/operando/webui/admin";
	public static final String PATH_OPERANDO_WEBUI_DASHBOARD = "/operando/webui/dashboard";
	public static final String PATH_OPERANDO_WEBUI_REPORTS = "/operando/webui/reports";
	public static final String PATH_OPERANDO_PDR_GATEKEEPER = "/operando/pdr/gatekeeper";
	public static final String PATH_OPERANDO_PDR_ACCESS_NODE = "/operando/pdr/access_node";
	public static final String PATH_OPERANDO_PDR_REPOSITORY = "/operando/pdr/repository";

	//RESTful endpoints for various modules
	public static final String ENDPOINT_POLICY_COMPUTATION_REGULATIONS_VARIABLE_REG_ID = PATH_OPERANDO_CORE_POLICY_COMPUTATION + "/regulations/%d";
	public static final String ENDPOINT_OSP_ENFORCEMENT_REGULATIONS_VARIABLE_REG_ID = PATH_OPERANDO_CORE_OSP_ENFORCEMENT + "/regulations/%d";
	public static final String ENDPOINT_OSP_ENFORCEMENT_PRIVACY_SETTINGS_VARIABLE_OSP_ID = PATH_OPERANDO_CORE_OSP_ENFORCEMENT + "/osps/%d/privacy_settings";
	public static final String ENDPOINT_USER_DEVICE_ENFORCEMENT_PRIVACY_SETTINGS = PATH_OPERANDO_CORE_DEVICE_ENFORCEMENT + "/privacy_settings";
	public static final String ENDPOINT_EMAIL_SERVICES_EMAIL_NOTIFICATION = PATH_OPERANDO_INTERFACES_EMAIL_SERVICES + "/email_notification";
	public static final String ENDPOINT_POLICY_DB_REGULATIONS = PATH_OPERANDO_CORE_POLICIES_DB + "/regulations";
	public static final String ENDPOINT_POLICY_DB_REGULATIONS_VARIABLE_REG_ID = ENDPOINT_POLICY_DB_REGULATIONS + "/%d";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID = PATH_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/deals/%d";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID_ACKNOWLEDGEMENT = ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID + "/acknowledgement";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS = PATH_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/offers";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS_VARIABLE_OFFER_ID = ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS + "/%d";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OSPS_VARIABLE_OSP_ID_DEALS = PATH_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/osps/%d/deals";
	public static final String ENDPOINT_REPORT_GENERATOR_REPORTS = PATH_OPERANDO_WEBUI_REPORTS + "/reports";

	private Client client = ClientBuilder.newClient();

	public Client getClient()
	{
		return client;
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
	public <T> T getStringJsonFollowingOperandoConventions(String strJson, Type typeOfT)
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