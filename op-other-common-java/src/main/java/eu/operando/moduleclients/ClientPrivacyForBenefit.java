package eu.operando.moduleclients;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import eu.operando.api.model.PfbDeal;
import eu.operando.api.model.PfbOffer;

public class ClientPrivacyForBenefit extends ClientOperandoModule
{
	private static final String PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT = "/operando/core/privacy_for_benefit";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID = PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/deals/%d";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID_ACKNOWLEDGEMENT =
			ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID + "/acknowledgement";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS = PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/offers";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS_VARIABLE_OFFER_ID = ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS + "/%d";
	public static final String ENDPOINT_PRIVACY_FOR_BENEFIT_OSPS_VARIABLE_OSP_ID_DEALS = PATH_INTERNAL_OPERANDO_CORE_PRIVACY_FOR_BENEFIT + "/osps/%d/deals";
	
	public ClientPrivacyForBenefit(String originPrivacyForBenefit)
	{
		super(originPrivacyForBenefit);
	}

	public void createPfbOffer(PfbOffer offer)
	{
		sendRequest(HttpMethod.POST, ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS, offer);
	}

	public void getPfbOffer(int offerId)
	{
		String endpoint = String.format(ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS_VARIABLE_OFFER_ID, offerId);
		sendRequest(HttpMethod.GET, endpoint);
	}

	public void updatePfbOffer(PfbOffer offer)
	{
		String endpoint = String.format(ENDPOINT_PRIVACY_FOR_BENEFIT_OFFERS_VARIABLE_OFFER_ID, offer.getId());
		sendRequest(HttpMethod.PUT, endpoint, offer);
	}

	public void getOspDeals(int ospId)
	{
		String endpoint = String.format(ENDPOINT_PRIVACY_FOR_BENEFIT_OSPS_VARIABLE_OSP_ID_DEALS, ospId);
		sendRequest(HttpMethod.GET, endpoint);
	}

	public PfbDeal getPfbDeal(int dealId)
	{
		String endpoint = String.format(ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID, dealId);
		Response response = sendRequest(HttpMethod.GET, endpoint);
		String strJson = response.readEntity(String.class);

		// Turn the JSON from the response into a deal.
		PfbDeal deal = createObjectFromJsonFollowingOperandoConventions(strJson, PfbDeal.class);
		return deal;
	}

	public void createPfbDealAcknowledgement(int dealId, int ospId, int token)
	{		
		String path = String.format(ENDPOINT_PRIVACY_FOR_BENEFIT_DEALS_VARIABLE_DEAL_ID_ACKNOWLEDGEMENT, dealId);		
		MultivaluedStringMap queryParameters = new MultivaluedStringMap();
		queryParameters.add("osp_id", "" + ospId);
		sendRequest(HttpMethod.POST, path, null, queryParameters);
	}
}
