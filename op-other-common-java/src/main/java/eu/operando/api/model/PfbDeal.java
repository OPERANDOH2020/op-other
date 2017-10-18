package eu.operando.api.model;

import java.util.Date;

/**
 * A wrapper class for the acceptance of a deal offered by OSPs as part of the privacy for benefit function of OPERANDO.
 * 
 * It is used to make the JSON objects that are passed between modules; it is okay that the fields are unused.
 */
@SuppressWarnings("unused")
public class PfbDeal
{
	private static final Date DATE_BEGINNING_OF_TIME = new Date(0);

	private int id = -1;
	private int userId = -1;
	private int offerId = -1;
	private Date createdAt = DATE_BEGINNING_OF_TIME;
	private Date canceledAt = DATE_BEGINNING_OF_TIME;

	public PfbDeal()
	{
	}

	public PfbDeal(int id, int userId, int offerId, Date createdAt, Date canceledAt)
	{
		this.id = id;
		this.userId = userId;
		this.offerId = offerId;
		this.createdAt = createdAt;
		this.canceledAt = canceledAt;
	}

}
