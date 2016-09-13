package eu.operando.api.model;

public class PrivacyPolicy
{
	private String id = "";
	private String terms = "";

	public PrivacyPolicy(String id, String terms)
	{
		this.id = id;
		this.terms = terms;		
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((terms == null) ? 0 : terms.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivacyPolicy other = (PrivacyPolicy) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (terms == null)
		{
			if (other.terms != null)
				return false;
		}
		else if (!terms.equals(other.terms))
			return false;
		return true;
	}
}
