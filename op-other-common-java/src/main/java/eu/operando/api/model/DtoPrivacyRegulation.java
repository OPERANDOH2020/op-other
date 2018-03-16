package eu.operando.api.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A privacy rule that reflects a given privacy legislation as described by a particular set of laws in a given jurisdiction.
 **/

@ApiModel(description = "A privacy rule that reflects a given privacy legislation as described by a particular set of laws in a given jurisdiction. ")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-12T14:06:26.001Z")
public abstract class DtoPrivacyRegulation
{

	private String legislationSector = "";
	private String reason = "";
	private PrivateInformationTypeEnum privateInformationType = null;
	private String action = "";
	private RequiredConsentEnum requiredConsent = null;

	/**
	 * Zero-argument constructor for JAX-B
	 */
	public DtoPrivacyRegulation()
	{
	}

	/**
	 * The type of private information; e.g. is it information that identifies the user (e.g. id number)? is it location information about the user?
	 * Is it about their activities?
	 */
	public enum PrivateInformationTypeEnum
	{
		@SerializedName("Identification")
		IDENTIFICATION("Identification"),
		@SerializedName("Shared Identification")
		SHARED_IDENTIFICATION("Shared Identification"),
		@SerializedName("Geographic")
		GEOGRAPHIC("Geographic"),
		@SerializedName("Temporal")
		TEMPORAL("Temporal"),
		@SerializedName("Network and relationships")
		NETWORK_AND_RELATIONSHIPS("Network and relationships"),
		@SerializedName("Objects")
		OBJECTS("Objects"),
		@SerializedName("Behavioural")
		BEHAVIOURAL("Behavioural"),
		@SerializedName("Beliefs")
		BELIEFS("Beliefs"),
		@SerializedName("Measurements")
		MEASUREMENTS("Measurements");

		private String value;

		PrivateInformationTypeEnum(String value)
		{
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString()
		{
			return String.valueOf(value);
		}
	}

	/**
	 * The type of consent that must be followed
	 */
	public enum RequiredConsentEnum
	{
		@SerializedName("opt-in")
		IN("opt-in"),
		@SerializedName("opt-out")
		OUT("opt-out");

		private String value;

		RequiredConsentEnum(String value)
		{
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString()
		{
			return String.valueOf(value);
		}
	}

	public DtoPrivacyRegulation(String legislationSector, String reason, PrivateInformationTypeEnum privateInformationType, String action,
			RequiredConsentEnum requiredConsent)
	{
		this.legislationSector = legislationSector;
		this.reason = reason;
		this.privateInformationType = privateInformationType;
		this.action = action;
		this.requiredConsent = requiredConsent;
	}

	/**
	 * The identifier of the set of legislation rules this rule belongs to e.g. the UK data protection act.
	 **/
	public DtoPrivacyRegulation legislationSector(String legislationSector)
	{
		this.legislationSector = legislationSector;
		return this;
	}

	@ApiModelProperty(required = true, value = "The identifier of the set of legislation rules this rule belongs to e.g. the UK data protection act. ")
	@JsonProperty("legislation_sector")
	public String getLegislationSector()
	{
		return legislationSector;
	}

	public void setLegislationSector(String legislationSector)
	{
		this.legislationSector = legislationSector;
	}

	/**
	 * The purpose for performing an action on data e.g. medical care, marketing, admin
	 **/
	public DtoPrivacyRegulation reason(String reason)
	{
		this.reason = reason;
		return this;
	}

	@ApiModelProperty(value = "The purpose for performing an action on data e.g. medical care, marketing, admin")
	@JsonProperty("reason")
	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	/**
	 * The type of private information; e.g. is it information that identifies the user (e.g. id number)? is it location information about the user?
	 * Is it about their activities?
	 **/
	public DtoPrivacyRegulation privateInformationType(PrivateInformationTypeEnum privateInformationType)
	{
		this.privateInformationType = privateInformationType;
		return this;
	}

	@ApiModelProperty(
			value = "The type of private information; e.g. is it information that identifies the user (e.g. id number)? is it location information about the user? Is it about their activities? ")
	@JsonProperty("private_information_type")
	public PrivateInformationTypeEnum getPrivateInformationType()
	{
		return privateInformationType;
	}

	public void setPrivateInformationType(PrivateInformationTypeEnum privateInformationType)
	{
		this.privateInformationType = privateInformationType;
	}

	/**
	 * The action being carried out on the data
	 **/
	public DtoPrivacyRegulation action(String action)
	{
		this.action = action;
		return this;
	}

	@ApiModelProperty(value = "The action being carried out on the data")
	@JsonProperty("action")
	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	/**
	 * The type of consent that must be followed
	 **/
	public DtoPrivacyRegulation requiredConsent(RequiredConsentEnum requiredConsent)
	{
		this.requiredConsent = requiredConsent;
		return this;
	}

	@ApiModelProperty(value = "The type of consent that must be followed")
	@JsonProperty("required_consent")
	public RequiredConsentEnum getRequiredConsent()
	{
		return requiredConsent;
	}

	public void setRequiredConsent(RequiredConsentEnum requiredConsent)
	{
		this.requiredConsent = requiredConsent;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		DtoPrivacyRegulation privacyRegulationInput = (DtoPrivacyRegulation) o;
		return Objects.equals(legislationSector, privacyRegulationInput.legislationSector)
			&& Objects.equals(reason, privacyRegulationInput.reason)
			&& Objects.equals(privateInformationType, privacyRegulationInput.privateInformationType) && Objects.equals(action, privacyRegulationInput.action)
			&& Objects.equals(requiredConsent, privacyRegulationInput.requiredConsent);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(legislationSector, reason, privateInformationType, action, requiredConsent);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("class PrivacyRegulationInput {\n");

		sb.append("    legislationSector: ")
			.append(toIndentedString(legislationSector))
			.append("\n");
		sb.append("    reason: ")
			.append(toIndentedString(reason))
			.append("\n");
		sb.append("    privateInformationType: ")
			.append(toIndentedString(privateInformationType))
			.append("\n");
		sb.append("    action: ")
			.append(toIndentedString(action))
			.append("\n");
		sb.append("    requiredConsent: ")
			.append(toIndentedString(requiredConsent))
			.append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces (except the first line).
	 */
	private String toIndentedString(Object o)
	{
		if (o == null)
		{
			return "null";
		}
		return o.toString()
			.replace("\n", "\n    ");
	}
}
