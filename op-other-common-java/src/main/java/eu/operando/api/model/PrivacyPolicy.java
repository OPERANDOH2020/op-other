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
		this.ospPolicyId = id;
		this.policies = accessPolicies;
	}
	
	@JsonProperty("osp_policy_id")
	public String getId(){
		return ospPolicyId;
	}
	
	@JsonProperty("policies")
	public Vector<AccessPolicy> getAccessPolicies(){
		return policies;
	}
	
	private String ospPolicyId;
	
	private Vector<AccessPolicy> policies;
	
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
		boolean match = Objects.equals(ospPolicyId, privacyPolicy.ospPolicyId) 
			&& Objects.equals(policies.size(), privacyPolicy.policies.size());
		for(int i = 0; i < policies.size() && match; i++){
			match = Objects.equals(policies.elementAt(i), privacyPolicy.policies.elementAt(i));
		}
		return match;
	}
	
	public static class AccessPolicy{
		public AccessPolicy(String reasonId, String dataUser, String dataSubjectType, String dataType, String reason){
			this.reasonid = reasonId;
			this.datauser = dataUser;
			this.datasubjecttype = dataSubjectType;
			this.datatype = dataType;
			this.reason = reason;
		}
		
		@JsonProperty("reasonid")
		public String getId(){
			return reasonid;
		}
		
		@JsonProperty("datauser")
		public String getDataUser(){
			return datauser;
		}
		
		@JsonProperty("datasubjecttype")
		public String getDataSubjectType(){
			return datasubjecttype;
		}
		
		@JsonProperty("datatype")
		public String getDataType(){
			return datatype;
		}
		
		@JsonProperty("reason")
		public String getReason(){
			return reason;
		}
		
		private String reasonid;
		
		private String datauser;
		
		private String datasubjecttype;
		
		private String datatype;
		
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
			return Objects.equals(reasonid, accessPolicy.reasonid)
					&& Objects.equals(datauser, accessPolicy.datauser)
					&& Objects.equals(datasubjecttype, accessPolicy.datasubjecttype)
					&& Objects.equals(datatype, accessPolicy.datatype)
					&& Objects.equals(reason, accessPolicy.reason);
		}
	};
}
