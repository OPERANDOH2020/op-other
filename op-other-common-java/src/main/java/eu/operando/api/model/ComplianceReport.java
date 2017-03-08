package eu.operando.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

// TODO correct this string
@ApiModel(description = "A report on an OSP's compliance with whatever it is")
public class ComplianceReport {
	
	private PrivacyPolicy policy;
	
	public ComplianceReport(PrivacyPolicy policy){
		this.policy = policy;
	}
	
	@JsonProperty("privacypolicy")
	public PrivacyPolicy getPrivacyPolicy(){
		return policy;
	}
	
}
