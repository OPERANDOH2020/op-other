package eu.operando.api.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class PrivacyRegulationInput extends DtoPrivacyRegulation
{
	/**
	 * Zero-argument constructor for JAX-B
	 */
	public PrivacyRegulationInput()
	{
		super();
	}

	public PrivacyRegulationInput(String legislationSector, String reason, PrivateInformationTypeEnum privateInformationType, String action,
			RequiredConsentEnum requiredConsent)
	{
		super(legislationSector, reason, privateInformationType, action, requiredConsent);
	}
}
