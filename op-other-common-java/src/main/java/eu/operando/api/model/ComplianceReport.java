package eu.operando.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "A report to assist checking an OSP's compliance with regulations")
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
