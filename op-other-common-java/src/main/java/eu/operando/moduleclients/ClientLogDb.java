package eu.operando.moduleclients;

import javax.ws.rs.HttpMethod;

import eu.operando.api.model.LogOperando;

public class ClientLogDb extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_LOG_DB = "/operando/core/log_db";
	private static final String ENDPOINT_LOG_DB_LOG = PATH_INTERNAL_OPERANDO_CORE_LOG_DB + "/log";

	public ClientLogDb(String originLogDb)
	{
		super(originLogDb);
	}

	public void logActivity(LogOperando logOperando)
	{
		sendRequest(HttpMethod.POST, ENDPOINT_LOG_DB_LOG, logOperando);
	}
}
