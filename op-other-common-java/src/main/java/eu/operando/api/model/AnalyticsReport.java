package eu.operando.api.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "TODO")
public class AnalyticsReport {
	public AnalyticsReport(String id, String name, String description, String base64){
		this.id = id;
		this.name = name;
		this.description = description;
		this.base64 = base64;
	}
	
	@JsonProperty("ID")
	public String getId(){
		return id;
	}
	
	@JsonProperty("Name")
	public String getName(){
		return name;
	}
	
	@JsonProperty("Description")
	public String getDescription(){
		return description;
	}
	
	@JsonProperty("Base64")
	public String getBase64(){
		return base64;
	}
	
	private String id;
	
	private String name;
	
	private String description;
	
	private String base64;
	
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
		AnalyticsReport analyticsReport = (AnalyticsReport) o;
		return Objects.equals(id, analyticsReport.id)
			&& Objects.equals(name, analyticsReport.name)
			&& Objects.equals(description, analyticsReport.description)
			&& Objects.equals(base64, analyticsReport.base64);
	}
}
