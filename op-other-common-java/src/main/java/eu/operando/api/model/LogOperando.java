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
}
