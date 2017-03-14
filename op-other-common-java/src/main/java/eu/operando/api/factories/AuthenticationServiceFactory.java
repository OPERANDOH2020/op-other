package eu.operando.api.factories;

import eu.operando.Utils;
import eu.operando.api.AuthenticationService;
import eu.operando.api.impl.AuthenticationServiceImpl;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoService;

public class AuthenticationServiceFactory {
	
	private static final String PROPERTY_NAME_ORIGIN_AUTHENTICATION_API = "originAuthenticationApi";
	
	public static AuthenticationService getAuthenticationService(String propertyFileName){
		
		// Property file property values.
		String originAuthenticationApi = Utils.loadPropertyString(propertyFileName, PROPERTY_NAME_ORIGIN_AUTHENTICATION_API);
		
		// Create the clients based on the properties file.
		ClientAuthenticationApiOperandoService clientAuthenticationService = 
				new ClientAuthenticationApiOperandoService(originAuthenticationApi);
		
		// Configure the service.
		return new AuthenticationServiceImpl(clientAuthenticationService);
	};
	
}
