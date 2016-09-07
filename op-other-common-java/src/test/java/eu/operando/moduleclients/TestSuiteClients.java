package eu.operando.moduleclients;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses
({
	ClientAuthenticationServiceTests.class,
	ClientBigDataAnalyticsTests.class,
	ClientDataAccessNodeTests.class,
	ClientEmailServicesTests.class,
	ClientLogDbTests.class,
	ClientOspEnforcementTests.class,
	ClientPolicyComputationTests.class,
	ClientPolicyDbTests.class,
	ClientPrivacyForBenefitTests.class,
	ClientReportGeneratorTests.class,
	ClientUserDeviceEnforcementTests.class,
	ClientWebCrawlerTests.class
})
public class TestSuiteClients
{

}
