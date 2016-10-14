package eu.operando;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtils
{
	private XmlUtils()
	{
		// class is a repository for static utility methods, so should not be instantiated.
	}
	
	/**
	 * Parses a string of xml, and returns an object representing the node tree.
	 * @param xml
	 * 	the xml string to parse
	 * @param supportNamespaces
	 * 	whether the returned document object should understand namespaces
	 * @return
	 * 	the document representing the tree of xml nodes.
	 * @throws XmlParseException 
	 * 	if there is an error whilst trying to parse.
	 */
	public static Document parseXmlString(String xml, boolean supportNamespaces) throws XmlParseException
	{
		Document document = null;
		
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(supportNamespaces);
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			document = builder.parse(is);
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			e.printStackTrace();
			throw new XmlParseException("Parsing the xml\n" + xml + "\nfailed.", e);
		}
		
		return document;
	}
	
	@SuppressWarnings("serial")
	public static class XmlParseException extends Exception
	{
		public XmlParseException(String message)
		{
			super(message);
		}
		
		public XmlParseException(String message, Exception e)
		{
			super(message, e);
		}
	}
}
