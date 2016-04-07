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

import org.junit.Rule;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public abstract class OperandoModuleClientTests extends OperandoModuleClient
{
	public static final String PREFIX_HTTP = "http://";
	public static final int PORT_WIREMOCK = 8089;
	public static final String HOST_WIREMOCK = "localhost:" + PORT_WIREMOCK;
	public static final String PROTOCOL_AND_HOST = PREFIX_HTTP + HOST_WIREMOCK;
	
	public static final String URL_AUTHENTICATION_SERVICE = PROTOCOL_AND_HOST + PATH_OPERANDO_CORE_AUTHENTICATION;
	public static final String URL_RIGHTS_MANAGEMENT = PROTOCOL_AND_HOST + PATH_OPERANDO_CORE_RIGHTS_MNG;
	public static final String URL_DATA_ACCESS_NODE = PROTOCOL_AND_HOST + PATH_OPERANDO_PDR_ACCESS_NODE;
	public static final String URL_LOG_DB = PROTOCOL_AND_HOST + PATH_OPERANDO_CORE_LOG_DB;
	public static final String URL_REPORT_GENERATOR = PROTOCOL_AND_HOST + PATH_OPERANDO_WEBUI_REPORTS;
	public static final String URL_POLICY_COMPUTATION = PROTOCOL_AND_HOST + PATH_OPERANDO_CORE_POLICY_COMPUTATION;
	public static final String URL_OSP_ENFORCEMENT = PROTOCOL_AND_HOST + PATH_OPERANDO_CORE_OSP_ENFORCEMENT;
	public static final String URL_USER_DEVICE_ENFORCEMENT = PROTOCOL_AND_HOST + PATH_OPERANDO_CORE_DEVICE_ENFORCEMENT;
	public static final String URL_EMAIL_SERVICES = PROTOCOL_AND_HOST + PATH_OPERANDO_INTERFACES_EMAIL_SERVICES;
	public static final String URL_POLICY_DB = PROTOCOL_AND_HOST + PATH_OPERANDO_CORE_POLICIES_DB;
	public static final String URL_BIG_DATA_ANALYTICS = PROTOCOL_AND_HOST + PATH_OPERANDO_CORE_BIGDATA;
	public static final String URL_PRIVACY_FOR_BENEFIT = PROTOCOL_AND_HOST + PATH_OPERANDO_CORE_PRIVACY_FOR_BENEFIT;
	
	private WireMockRule wireMockRule = new WireMockRule(PORT_WIREMOCK);

	@Rule
	public WireMockRule getWireMockRule()
	{
		return wireMockRule;
	}
}
