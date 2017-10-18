package eu.operando.api.model;

/**
 * A wrapper class for logs.
 * 
 * It is used to make the JSON objects that are passed between modules; it is okay that the fields are unused.
 */
@SuppressWarnings("unused")
public class LogOperando
{

	private String requesterType;
	private String requesterId;
	private String logPriority;
	private String logDataType;
	private String title;
	private String description;

	public LogOperando()
	{

	}

	public LogOperando(String requesterType, String requesterId, String logPriority, String logDataType, String title, String description)
	{
		this.requesterType = requesterType;
		this.requesterId = requesterId;
		this.logPriority = logPriority;
		this.logDataType = logDataType;
		this.title = title;
		this.description = description;
	}

	/*
	 * public String getRequesterType() { return requesterType; } public void setRequesterType(String requesterType) { this.requesterType =
	 * requesterType; } public String getRequesterId() { return requesterId; } public void setRequesterId(String requesterId) { this.requesterId =
	 * requesterId; } public String getLogPriority() { return logPriority; } public void setLogPriority(String logPriority) { this.logPriority =
	 * logPriority; } public String getLogDataType() { return logDataType; } public void setLogDataType(String logDataType) { this.logDataType =
	 * logDataType; } public String getTitle() { return title; } public void setTitle(String title) { this.title = title; } public String
	 * getDescription() { return description; } public void setDescription(String description) { this.description = description; }
	 */
}
