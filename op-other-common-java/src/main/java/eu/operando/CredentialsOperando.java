package eu.operando;

@SuppressWarnings("unused") // This class is only used for data transfer, so the fields do not need to be used.
public class CredentialsOperando
{
	private String username = "";
	private String password = "";

	public CredentialsOperando(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
}
