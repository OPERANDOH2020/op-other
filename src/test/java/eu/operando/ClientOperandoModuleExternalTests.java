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
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

/**
 * For testing the code in ClientOperandoModuleExternal.
 */
public class ClientOperandoModuleExternalTests extends ClientOperandoModuleTests
{
	public void testAuthoriseOsp_CorrectHttpRequest(ClientOperandoModuleExternal client)
	{
		//Set up
		String serviceTicket = "qwerty1234";
		String endpoint = String.format(ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION, serviceTicket);
		getWireMockRule().stubFor(get(urlPathEqualTo(endpoint))
				.willReturn(aResponse()));
		
		//Exercise
		client.authoriseOsp(serviceTicket);
		
		//Verify
		getWireMockRule().verify(getRequestedFor(urlPathEqualTo(endpoint)));
	}
	
	public void testLogActivity_CorrectHttpRequest(ClientOperandoModuleExternal client)
	{
		//Set up
		LogOperando logOperando = new LogOperando("requesterType", "ID", "priority", "dataType", "title", "desc");
		
		String endpoint = ENDPOINT_LOG_DB_LOG;
		getWireMockRule().stubFor(post(urlPathEqualTo(endpoint))
				.willReturn(aResponse()));
		
		//Exercise
		client.logActivity(logOperando);
		
		//Verify
		String stringJsonLog = getStringJsonFollowingOperandoConventions(logOperando);
		getWireMockRule().verify(postRequestedFor(urlPathEqualTo(endpoint))
				.withRequestBody(equalToJson(stringJsonLog)));
	}
}
