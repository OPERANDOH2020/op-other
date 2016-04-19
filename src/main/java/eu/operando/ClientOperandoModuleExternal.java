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

/**
 * Represents the client of an OPERANDO module which is accessible to those outside of the OPERANDO platform.
 * @see ClientOperandoModule
 */
public abstract class ClientOperandoModuleExternal extends ClientOperandoModule
{
	private String protocolAndHostAuthenticationService = "";
	private String protocolAndHostLogDb = "";

	public ClientOperandoModuleExternal(String protocolAndHostAuthenticationService, String protocolAndHostLogDb)
	{
		this.protocolAndHostAuthenticationService = protocolAndHostAuthenticationService;
		this.protocolAndHostLogDb = protocolAndHostLogDb;		
	}

	public void authoriseOsp(String serviceTicket)
	{
		//Create a web target for the correct end-point.
		String endpoint = String.format(ENDPOINT_AUTHENTICATION_API_SERVICE_TICKETS_VARIABLE_TICKET_VALIDATION, serviceTicket);
		WebTarget target = getClient().target(protocolAndHostAuthenticationService);
		target = target.path(endpoint);
		
		//Send the request.
		Builder requestBuilder = target.request();
		requestBuilder.get();
	}

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
