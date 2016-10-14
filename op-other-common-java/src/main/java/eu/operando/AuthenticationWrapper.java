package eu.operando;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.operando.XmlUtils.XmlParseException;

public class AuthenticationWrapper
{
	private static final Logger LOGGER = LogManager.getLogger(AuthenticationWrapper.class);

	// XML constants
	private static final String XML_NAMESPACE_OPERANDO_CAS = "http://www.yale.edu/tp/cas";
	private static final String XML_TAG_NAME_INDICATING_INVALID_TICKET = "authenticationFailure";
	private static final String XML_TAG_NAME_INDICATING_VALID_TICKET = "authenticationSuccess";
	private static final String XML_TAG_NAME_USER = "user";

	private boolean validTicket = false;
	private String idOspUser = null;

	public AuthenticationWrapper(boolean validTicket, String idOspUser)
	{
		this.validTicket = validTicket;
		this.idOspUser = idOspUser;
	}

	public boolean isTicketValid()
	{
		return validTicket;
	}

	public String getIdOspUser()
	{
		return idOspUser;
	}

	public static AuthenticationWrapper fromXml(String xml) throws XmlParseException
	{
		boolean validTicket = false;
		String idOspUser = "";
		Document document;

		document = XmlUtils.parseXmlString(xml, true);
		if (validXml(document))
		{
			validTicket = isCallerAuthenticated(document);
			if (validTicket)
			{
				idOspUser = extractIdOspUser(document);
			}
		}
		else
		{
			LOGGER.error("The XML returned from the authentication service doesn't match the assumptions that this code was built upon. XML:\n" + xml);
			throw new XmlParseException("XML from authentication service not vaild.");
		}

		return new AuthenticationWrapper(validTicket, idOspUser);
	}

	/**
	 * Check that the xml follows the agreed format, otherwise we may not parse correctly.
	 * 
	 * @param document
	 *        a document object representing the xml.
	 * @return whether the xml follows the expected format.
	 */
	private static boolean validXml(Document document)
	{
		boolean xmlAsExpected = false;

		NodeList nodesSuccess = document.getElementsByTagNameNS(XML_NAMESPACE_OPERANDO_CAS, XML_TAG_NAME_INDICATING_VALID_TICKET);
		NodeList nodesFailure = document.getElementsByTagNameNS(XML_NAMESPACE_OPERANDO_CAS, XML_TAG_NAME_INDICATING_INVALID_TICKET);
		NodeList nodesUser = document.getElementsByTagNameNS(XML_NAMESPACE_OPERANDO_CAS, XML_TAG_NAME_USER);

		int numberOfSuccessNodes = nodesSuccess.getLength();
		int numberOfFailureNodes = nodesFailure.getLength();
		int numberOfUserNodes = nodesUser.getLength();

		if (numberOfSuccessNodes + numberOfFailureNodes == 1 && numberOfSuccessNodes == numberOfUserNodes)
		{
			xmlAsExpected = true;
		}

		return xmlAsExpected;
	}

	/**
	 * Checks whether the xml represents that the ticket was successfully authenticated.
	 * 
	 * @param document
	 *        a document object representing the xml.
	 * @return whether the xml indicates successful authentication.
	 */
	private static boolean isCallerAuthenticated(Document document)
	{
		boolean validTicket = false;

		NodeList nodesSuccess = document.getElementsByTagNameNS(XML_NAMESPACE_OPERANDO_CAS, XML_TAG_NAME_INDICATING_VALID_TICKET);
		int numberOfSuccessNodes = nodesSuccess.getLength();
		if (numberOfSuccessNodes == 1)
		{
			validTicket = true;
		}

		return validTicket;
	}

	/**
	 * Searches the xml document for a string identifying the user represented by the ticket.
	 * 
	 * @param document
	 *        a document object representing the xml.
	 * @return a string identifying the user represented by the ticket.
	 */
	private static String extractIdOspUser(Document document)
	{
		String idOspUser = null;

		NodeList nodesUser = document.getElementsByTagNameNS(XML_NAMESPACE_OPERANDO_CAS, XML_TAG_NAME_USER);
		Node nodeUser = nodesUser.item(0);
		idOspUser = nodeUser.getTextContent();

		return idOspUser;
	}
}
