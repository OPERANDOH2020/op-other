package eu.operando.api.model;

/**
 * OPERANDO's internal object representing a privacy setting with an OSP.
 * Returned by the UDE and OSE modules' APIs.
 */
public class PrivacySetting
{
	private int id = -1; // PrivacySetting Unique Identifier
	private String description = ""; // Description of the setting
	private String name = ""; // Short name of the setting(e.g. visibility)
	private String settingKey = ""; // Targeted setting key
	private String settingValue = ""; // Targeted setting value

	public PrivacySetting(int id, String description, String name, String settingKey, String settingValue)
	{
		this.id = id;
		this.description = description;
		this.name = name;
		this.settingKey = settingKey;
		this.settingValue = settingValue;
	}

	/**
	 * Generated using Eclipse.
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((settingKey == null) ? 0 : settingKey.hashCode());
		result = prime * result + ((settingValue == null) ? 0 : settingValue.hashCode());
		return result;
	}

	/**
	 * Two PrivacySettings are equal if all of their fields are equal (both null or .equals())
	 * Generated using Eclipse.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		
		PrivacySetting other = (PrivacySetting) obj;
		
		if (description == null)
		{
			if (other.description != null)
			{
				return false;
			}
		}
		else if (!description.equals(other.description))
		{
			return false;
		}
		
		
		if (id != other.id)
		{
			return false;
		}
		
		if (name == null)
		{
			if (other.name != null)
			{
				return false;
			}
		}
		else if (!name.equals(other.name))
		{
			return false;
		}
		
		if (settingKey == null)
		{
			if (other.settingKey != null)
			{
				return false;
			}
		}
		else if (!settingKey.equals(other.settingKey))
		{
			return false;
		}
		
		if (settingValue == null)
		{
			if (other.settingValue != null)
			{
				return false;
			}
		}
		else if (!settingValue.equals(other.settingValue))
		{
			return false;
		}
		
		return true;
	}

}