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

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

/**
 * Represents the client of an OPERANDO module which is accessible to those outside of the OPERANDO platform.
 * @see ClientOperandoModule
 */
public abstract class ClientOperandoModuleExternal extends ClientOperandoModule
{
	private String protocolAndHostAuthenticationApi = "";
	private String protocolAndHostLogDb = "";

	public ClientOperandoModuleExternal(String protocolAndHostAuthenticationApi, String protocolAndHostLogDb)
	{
		this.protocolAndHostAuthenticationApi = protocolAndHostAuthenticationApi;
		this.protocolAndHostLogDb = protocolAndHostLogDb;		
	}

	/**
	 * Authentication Service 
	 */
	public boolean isOspAuthenticated(String serviceTicket)
	{
		boolean validTicket = false;
		
		//Create a web target for the correct end-point.
		String endpoint = String.format(ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION, serviceTicket);
		WebTarget target = getClient().target(protocolAndHostAuthenticationApi);
		target = target.path(endpoint);
		
		//Send the request.
		Builder requestBuilder = target.request();
		Response response = requestBuilder.get();
		
		//Interpret the response.
		int status = response.getStatus();
		if (status == HttpStatus.SC_OK)
		{
			validTicket = true;
		}
		
		return validTicket;
	}
	
	/**
	 * Log DB
	 */
	public void logActivity(LogOperando logOperando)
	{
		//Create a web target for the correct end-point.
		WebTarget target = getClient().target(protocolAndHostLogDb);
		target = target.path(ENDPOINT_LOG_DB_LOG);

		//Send the request.
		Builder requestBuilder = target.request();
		requestBuilder.post(createEntityStringJsonFromObject(logOperando));
	}
}
