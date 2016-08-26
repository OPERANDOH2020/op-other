package eu.operando;

/**
 * The OperandoCommunicatonException is designed to be used for exceptional cases during OPERANDO module-module communication. Such a case might be if
 * a resource is requested from another module, but that module is not able to find the requested resource.
 */
@SuppressWarnings("serial")
public class OperandoCommunicationException extends Exception
{
	private CommunicationError error = null;

	public enum CommunicationError
	{
		REQUESTED_RESOURCE_NOT_FOUND,
		ERROR_FROM_OTHER_MODULE
	}

	public OperandoCommunicationException(CommunicationError error)
	{
		this.error = error;
	}

	public CommunicationError getCommunitcationError()
	{
		return error;
	}
}
