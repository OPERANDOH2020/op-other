package eu.operando;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;

public final class Utils
{
	private Utils()
	{
		throw new AssertionError("This class should not be instantiated; it is a Util class.");
	}
	
	/**
	 * Returns the value of the property from PROPERTIES_FILE with name equal to propertyName parsed as an int.
	 */
	public static int loadPropertyInt(String filename, String propertyName)
	{
		String propertyStr = loadPropertyString(filename, propertyName);
		return Integer.parseInt(propertyStr);
	}

	/**
	 * Returns the value of the property from the file at filename with name equal to propertyName.
	 * 
	 * If there is no property with the given name, the returned string is null.
	 * 
	 * @param filename the location of the properties file.
	 * @param propertyName the name of the property to be read.
	 * @return the value of the specified property in the specified file if it exists, otherwise null.
	 */
	public static String loadPropertyString(String filename, String propertyName) {
		String propertyValue = null;

		Properties properties = new Properties();
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
			//Load the properties from the file's stream.
			properties.load(inputStream);
			//Read the property for the given name.
			propertyValue = properties.getProperty(propertyName);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return propertyValue;
	}
	
	/**
	 * Checks to see if a status code is in a particular family.
	 */
	public static boolean statusCodeIsInFamily(int statusCode, Family family)
	{
		Status status = Status.fromStatusCode(statusCode);
		Family statusFamilyResponse = status.getFamily();
		boolean doesFamilyContainStatus = statusFamilyResponse.equals(family);
		return doesFamilyContainStatus;
	}
}
