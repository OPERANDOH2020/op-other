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

import javax.ws.rs.HttpMethod;

/**
 * For testing the code in ClientOperandoModuleExternal.
 */
public class ClientOperandoModuleExternalTests extends ClientOperandoModuleTests
{
	public void testAuthoriseOsp_CorrectHttpRequest(ClientOperandoModuleExternal client)
	{
		//Set up
		String serviceTicket = "qwerty1234";
		
		//Exercise
		client.authoriseOsp(serviceTicket);
		
		//Verify
		String endpoint = String.format(ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION, serviceTicket);
		verify(HttpMethod.GET, endpoint);
	}
	
	public void testLogActivity_CorrectHttpRequest(ClientOperandoModuleExternal client)
	{
		//Set up
		LogOperando logOperando = new LogOperando("requesterType", "ID", "priority", "dataType", "title", "desc");
		
		//Exercise
		client.logActivity(logOperando);
		
		//Verify
		verifyWithoutQueryParams(HttpMethod.POST, ENDPOINT_LOG_DB_LOG, logOperando);
	}
}
