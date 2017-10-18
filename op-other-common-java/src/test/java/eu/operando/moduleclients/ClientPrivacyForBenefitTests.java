package eu.operando.moduleclients;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Test;

import eu.operando.api.model.PfbDeal;
import eu.operando.api.model.PfbOffer;

public class ClientPrivacyForBenefitTests extends ClientOperandoModuleTests
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT = "/operando/core/privacy_for_benefit";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID = PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/deals/%d";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID_ACKNOWLEDGEMENT =
			ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID + "/acknowledgement";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS = PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/offers";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS_VARIABLE_OFFER_ID = ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS + "/%d";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OSPS_VARIABLE_OSP_ID_DEALS = PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/osps/%d/deals";

	private ClientPrivacyForBenefit client = new ClientPrivacyForBenefit(ORIGIN_WIREMOCK);
	
	@Test
	public void testGetPfbDeal_CorrectHttpRequest()
	{
		// Set Up
		int dealId = 1;
		String endpoint = String.format(ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID, dealId);
		stub(HttpMethod.GET, endpoint);

		// Exercise
		client.getPfbDeal(dealId);

		// Verify
		verifyCorrectHttpRequest(HttpMethod.GET, endpoint);
	}

	@Test
	public void testGetPfbDeal_ResponseHandledCorrectly()
	{
		// Set Up
		int id = 1;
		int userId = 2;
		int offerId = 3;
		Date createdAt = new Date(0);
		Date canceledAt = new Date(0);

		PfbDeal dealExpected = new PfbDeal(id, userId, offerId, createdAt, canceledAt);

		String endpoint = String.format(ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID, id);
		stub(HttpMethod.GET, endpoint, dealExpected);

		// Exercise
		PfbDeal dealActual = client.getPfbDeal(id);

		// Verify
		boolean isDealActualEqualToDealExpected = EqualsBuilder.reflectionEquals(dealExpected, dealActual);
		assertTrue("The client did not correctly interpret the deal JSON", isDealActualEqualToDealExpected);
	}

	@Test
	public void testCreatePfbDealAcknowledgement_CorrectHttpRequest()
	{
		// Set Up
		int dealId = 1;
		int ospId = 2;
		int token = 2;

		// Exercise
		client.createPfbDealAcknowledgement(dealId, ospId, token);

		// Verify
		MultivaluedMap<String, String> queriesParamToValue = new MultivaluedStringMap();
		queriesParamToValue.putSingle("osp_id", "" + ospId);
		String endpoint = String.format(ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID_ACKNOWLEDGEMENT, dealId);
		verifyCorrectHttpRequestWithQueryParams(HttpMethod.POST, endpoint, queriesParamToValue);
	}

	@Test
	public void testCreateOffer_CorrectHttpRequest()
	{
		// Set Up
		PfbOffer offer = new PfbOffer(1, 2, "title", "description", "serviceWebsite", true, "ospCallbackUrl", new Date());

		// Exercise
		client.createPfbOffer(offer);

		// Verify
		verifyCorrectHttpRequest(HttpMethod.POST, ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS, offer);
	}

	@Test
	public void testGetOffer_CorrectHttpRequest()
	{
		// Set Up
		int offerId = 1;

		// Exercise
		client.getPfbOffer(offerId);

		// Verify
		String endpoint = String.format(ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS_VARIABLE_OFFER_ID, offerId);
		verifyCorrectHttpRequest(HttpMethod.GET, endpoint);
	}

	@Test
	public void testUpdateOffer_CorrectHttpRequest()
	{
		// Set Up
		int offerId = 1;

		PfbOffer offer = new PfbOffer(offerId, 2, "title", "description", "serviceWebsite", true, "ospCallbackUrl", new Date());

		// Exercise
		client.updatePfbOffer(offer);

		// Verify
		String endpoint = String.format(ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS_VARIABLE_OFFER_ID, offerId);
		verifyCorrectHttpRequest(HttpMethod.PUT, endpoint, offer);
	}

	@Test
	public void testGetOspDeals_CorrectHttpRequest()
	{
		// Set Up
		int ospId = 1;

		// Exercise
		client.getOspDeals(ospId);

		// Verify
		String endpoint = String.format(ENDPOINT_PRIVACY_FOR_BENEFIT_OSPS_VARIABLE_OSP_ID_DEALS, ospId);
		verifyCorrectHttpRequest(HttpMethod.GET, endpoint);
	}
}
