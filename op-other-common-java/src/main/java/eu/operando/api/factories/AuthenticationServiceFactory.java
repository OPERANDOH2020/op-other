package eu.operando.api.factories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.operando.Utils;
import eu.operando.api.AuthenticationService;
import eu.operando.api.impl.AuthenticationServiceImpl;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoService;

public class AuthenticationServiceFactory {
	
	private static final String PROPERTY_NAME_ORIGIN_AUTHENTICATION_API = "originAuthenticationApi";
	
	public static AuthenticationService getAuthenticationService(String propertyFileName){
		
		// Property file property values.
		String originAuthenticationApi = Utils.loadPropertyString(propertyFileName, PROPERTY_NAME_ORIGIN_AUTHENTICATION_API);
		
		
		//TODO remove when no longer needed
		final Logger LOGGER = LogManager.getLogger(AuthenticationService.class);
		LOGGER.info("AuthenticationService started with base url: " + originAuthenticationApi);
		
		// Create the clients based on the properties file.
		ClientAuthenticationApiOperandoService clientAuthenticationService = 
				new ClientAuthenticationApiOperandoService(originAuthenticationApi);
		
		// Configure the service.
		return new AuthenticationServiceImpl(clientAuthenticationService);
	};
	
}
