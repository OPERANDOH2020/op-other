package eu.operando.api.model;

import java.util.Objects;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

// TODO correct this string
@ApiModel(description = "tbd")
public class PrivacyPolicy
{
	public PrivacyPolicy(String id, Vector<AccessPolicy> accessPolicies){
		this.id = id;
		this.accessPolicies = accessPolicies;
	}
	
	@JsonProperty("osp_policy_id")
	public String getId(){
		return id;
	}
	
	@JsonProperty("policies")
	public Vector<AccessPolicy> getAccessPolicies(){
		return accessPolicies;
	}
	
	private String id;
	
	private Vector<AccessPolicy> accessPolicies;
	
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
		PrivacyPolicy privacyPolicy = (PrivacyPolicy) o;
		boolean match = Objects.equals(id, privacyPolicy.id) 
			&& Objects.equals(accessPolicies.size(), privacyPolicy.accessPolicies.size());
		for(int i = 0; i < accessPolicies.size() && match; i++){
			match = Objects.equals(accessPolicies.elementAt(i), privacyPolicy.accessPolicies.elementAt(i));
		}
		return match;
	}
	
	public static class AccessPolicy{
		public AccessPolicy(String reasonId, String dataUser, String dataSubjectType, String dataType, String reason){
			this.reasonId = reasonId;
			this.dataUser = dataUser;
			this.dataSubjectType = dataSubjectType;
			this.dataType = dataType;
			this.reason = reason;
		}
		
		@JsonProperty("reasonid")
		public String getId(){
			return reasonId;
		}
		
		@JsonProperty("datauser")
		public String getDataUser(){
			return dataUser;
		}
		
		@JsonProperty("datasubjecttype")
		public String getDataSubjectType(){
			return dataSubjectType;
		}
		
		@JsonProperty("datatype")
		public String getDataType(){
			return dataType;
		}
		
		@JsonProperty("reason")
		public String getReason(){
			return reason;
		}
		
		private String reasonId;
		
		private String dataUser;
		
		private String dataSubjectType;
		
		private String dataType;
		
		private String reason;
		
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
			AccessPolicy accessPolicy = (AccessPolicy) o;
			return Objects.equals(reasonId, accessPolicy.reasonId)
					&& Objects.equals(dataUser, accessPolicy.dataUser)
					&& Objects.equals(dataSubjectType, accessPolicy.dataSubjectType)
					&& Objects.equals(dataType, accessPolicy.dataType)
					&& Objects.equals(reason, accessPolicy.reason);
		}
	};
}
