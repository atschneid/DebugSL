package misc;

public class GeneralException extends RuntimeException
{
	private static final long serialVersionUID = -2272867298064213165L;

	public GeneralException()
	{
		super();
	}

	public GeneralException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public GeneralException(String message)
	{
		super(message);
	}

	public GeneralException(Throwable cause)
	{
		super(cause);
	}

}
