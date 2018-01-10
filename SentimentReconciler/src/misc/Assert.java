package misc;

public class Assert
{
	private static final String EXCEPTION_MESSAGE_NOT_NULL = "Parameter can not be null. ";
	private static final String EXCEPTION_MESSAGE_IS_TRUE = "Condition must be [true]. ";

	public static void notNull(Object obj)
	{
		notNull(obj, null);
	}

	public static void notNull(Object obj, String message)
	{
		if (obj == null)
		{
			throw (message == null) ? new GeneralException(
					EXCEPTION_MESSAGE_NOT_NULL) : new GeneralException(
					EXCEPTION_MESSAGE_NOT_NULL + "[Message]" + message);
		}
	}

	public static void isTrue(boolean condition)
	{
		isTrue(condition, null);
	}

	public static void isTrue(boolean condition, String message)
	{
		if (!condition)
		{
			throw (message == null) ? new GeneralException(
					EXCEPTION_MESSAGE_IS_TRUE) : new GeneralException(
					EXCEPTION_MESSAGE_IS_TRUE + "[Message]" + message);
		}
	}
}
