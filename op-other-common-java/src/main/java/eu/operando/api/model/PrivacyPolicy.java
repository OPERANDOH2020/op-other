package eu.operando.api.model;

public class PrivacyPolicy
{
	private int id = -1;
	private String terms = "";

	public PrivacyPolicy(int id, String terms)
	{
		this.id = id;
		this.terms = terms;		
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTerms()
	{
		return terms;
	}

	public void setTerms(String terms)
	{
		this.terms = terms;
	}
		
}
