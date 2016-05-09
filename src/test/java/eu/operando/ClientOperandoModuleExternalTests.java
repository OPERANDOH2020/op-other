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

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import javax.ws.rs.HttpMethod;
import org.apache.http.HttpStatus;

/**
 * For testing the code in ClientOperandoModuleExternal.
 */
public class ClientOperandoModuleExternalTests extends ClientOperandoModuleTests
{
	/**
	 * Authentication API.
	 */
	public void testAuthoriseOsp_CorrectHttpRequest(ClientOperandoModuleExternal client)
	{
		//Set up
		String serviceTicket = "qwerty1234";
		
		//Exercise
		client.isOspAuthenticated(serviceTicket);
		
		//Verify
		String endpoint = String.format(ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION, serviceTicket);
		verifyCorrectHttpRequest(HttpMethod.GET, endpoint);
	}
	public void testAuthoriseOsp_HandleValidTicketCorrectly(ClientOperandoModuleExternal client)
	{
		//Set up
		String serviceTicket = "qwerty1234";
		String endpoint = String.format(ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION, serviceTicket);
		stub(HttpMethod.GET, endpoint, "", HttpStatus.SC_OK);
		
		//Exercise
		boolean	validTicket = client.isOspAuthenticated(serviceTicket);
		
		//Verify
		assertThat(validTicket, is(true));
	}
	public void testAuthoriseOsp_HandleInvalidTicketCorrectly(ClientOperandoModuleExternal client)
	{
		//Set up
		String serviceTicket = "qwerty1234";
		String endpoint = String.format(ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION, serviceTicket);
		stub(HttpMethod.GET, endpoint, "", HttpStatus.SC_BAD_REQUEST);

		//Exercise
		boolean	validTicket = client.isOspAuthenticated(serviceTicket);

		//Verify
		assertThat(validTicket, is(false));
	}
	/**
	 * TODO - handle other responses e.g. 404, 500
	 */
	
	/**
	 * Log DB.
	 */
	public void testLogActivity_CorrectHttpRequest(ClientOperandoModuleExternal client)
	{
		//Set up
		LogOperando logOperando = new LogOperando("requesterType", "ID", "priority", "dataType", "title", "desc");
		
		//Exercise
		client.logActivity(logOperando);
		
		//Verify
		verifyCorrectHttpRequestWithoutQueryParams(HttpMethod.POST, ENDPOINT_LOG_DB_LOG, logOperando);
	}
}
