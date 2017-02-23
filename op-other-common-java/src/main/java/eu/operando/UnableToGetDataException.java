package eu.operando;

/**
 * An exception to indicate that, for whatever reason, it's not possible to get the necessary data to complete this task.
 */
@SuppressWarnings("serial")
public class UnableToGetDataException extends Exception
{

	public UnableToGetDataException(Exception ex)
	{
		super(ex);
	}

}
