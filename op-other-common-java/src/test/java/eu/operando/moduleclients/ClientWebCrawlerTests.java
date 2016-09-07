package eu.operando.moduleclients;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Vector;

import javax.ws.rs.HttpMethod;

import org.junit.Test;

import eu.operando.api.model.PrivacyPolicy;

public class ClientWebCrawlerTests extends ClientTests
{
	private static final String PATH_INTERNAL_OPERANDO_INTERFACES_WEB_SERVICES = "/operando/interfaces/web_services";
	public static final String ENDPOINT_WEB_SERVICES_PRIVACY_POLICIES = PATH_INTERNAL_OPERANDO_INTERFACES_WEB_SERVICES + "/GetOSPPrivacyTerms";
	public static final String ENDPOINT_WEB_SERVICES_PRIVACY_OPTIONS = PATH_INTERNAL_OPERANDO_INTERFACES_WEB_SERVICES + "/GetOSPSettings";
	
	private ClientWebCrawler client = new ClientWebCrawler(ORIGIN_WIREMOCK);
	
	@Test
	public void testGetOspPrivacyPolicies_CorrectHttpRequest()
	{
		//Set up
		stub(HttpMethod.GET, ENDPOINT_WEB_SERVICES_PRIVACY_POLICIES);
		
		//Exercise
		client.getOspPrivacyPolicies();
		
		//Verify
		verifyCorrectHttpRequest(HttpMethod.GET, ENDPOINT_WEB_SERVICES_PRIVACY_POLICIES);
	}
	@Test
	public void testGetOspPrivacyPolicies_ResponseHandledCorrectly()
	{
		//Set up
		Vector<PrivacyPolicy> policiesExpected = new Vector<PrivacyPolicy>();
		policiesExpected.add(new PrivacyPolicy(1, "some text"));
		policiesExpected.add(new PrivacyPolicy(2, "some other text"));
		stub(HttpMethod.GET, ENDPOINT_WEB_SERVICES_PRIVACY_POLICIES, policiesExpected);
		
		//Exercise
		Vector<PrivacyPolicy> policiesActual = client.getOspPrivacyPolicies();
		
		//Verify
		//TODO - this fails at the moment because equals has not been overridden for privacy policy. It should be as we'll use it in the production code (WatchdogApplication) later. 
		assertThat("The policies returned from the web crawler were incorrectly interpreted by the WatchdogClient", policiesActual, is(equalTo(policiesExpected)));
	}
	@Test
	public void testGetOspPrivacyOptions_CorrectHttpRequest()
	{
		//Exercise
		client.getOspPrivacyOptions();

		//Verify
		verifyCorrectHttpRequest(HttpMethod.GET, ENDPOINT_WEB_SERVICES_PRIVACY_OPTIONS);
	}
}
