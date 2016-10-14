package eu.operando;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Utils
{
	private Utils()
	{
		throw new AssertionError("This class should not be instantiated; it is a Util class.");
	}
	
	/**
	 * Returns the value of the property from PROPERTIES_FILE with name equal to propertyName parsed as a boolean.
	 */
	public static boolean loadPropertyBool(String filename, String propertyName)
	{
		String propertyStr = loadPropertyString(filename, propertyName);
		return Boolean.parseBoolean(propertyStr);
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
	 * @param filename
	 *        the location of the properties file.
	 * @param propertyName
	 *        the name of the property to be read.
	 * @return the value of the specified property in the specified file if it exists, otherwise null.
	 */
	public static String loadPropertyString(String filename, String propertyName)
	{
		String propertyValue = null;

		Properties properties = new Properties();
		try (InputStream inputStream = Thread.currentThread()
			.getContextClassLoader()
			.getResourceAsStream(filename))
		{
			// Load the properties from the file's stream.
			properties.load(inputStream);
			// Read the property for the given name.
			propertyValue = properties.getProperty(propertyName);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return propertyValue;
	}
}
