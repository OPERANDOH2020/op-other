package eu.operando;

/**
 * An {@code OperandoAuthenticationException} should be thrown when OPERANDO-specific authentication fails. An example might be when the
 * Authentication Service doesn't recognise the credentials which a module provides to authenticate itself.
 */
@SuppressWarnings("serial")
public class OperandoAuthenticationException extends Exception
{
	public OperandoAuthenticationException(String message)
	{
		super(message);
	}
}
