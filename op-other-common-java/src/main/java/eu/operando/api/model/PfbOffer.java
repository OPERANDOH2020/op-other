package eu.operando.api.model;

import java.util.Date;

/**
 * A wrapper class for a the offer of a deal by OSPs as part of the privacy for benefit function of OPERANDO.
 * 
 * It is used to make the JSON objects that are passed between modules; it is okay that the fields are unused.
 */
@SuppressWarnings("unused")
public class PfbOffer
{
	private static final Date DATE_START_OF_TIME = new Date(0);
	private int id = -1;
	private int ospId = -1;
	private String title = "";
	private String description = "";
	private String serviceWebsite = "";
	private boolean isEnabled = false;
	private String ospCallbackUrl = "";
	private Date expirationDate = DATE_START_OF_TIME;

	public PfbOffer()
	{
	}

	public PfbOffer(int id, int ospId, String title, String description, String serviceWebsite, boolean isEnabled, String ospCallbackUrl, Date expirationDate)
	{
		this.id = id;
		this.ospId = ospId;
		this.title = title;
		this.description = description;
		this.serviceWebsite = serviceWebsite;
		this.isEnabled = isEnabled;
		this.ospCallbackUrl = ospCallbackUrl;
		this.expirationDate = expirationDate;
	}

	public int getId()
	{
		return id;
	}
}
