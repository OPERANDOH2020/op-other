package eu.operando.api.model;

/**
 * A wrapper class for email attachments.
 * 
 * It is used to make the JSON objects that are passed between modules;
 * it is okay that the fields are unused. 
 */
@SuppressWarnings("unused")
public class Attachment
{
	private String title = "";
	private String path = "";
}
