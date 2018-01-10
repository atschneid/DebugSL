package misc;

import java.lang.reflect.Constructor;

public abstract class ClassFactory
{
	public static <T> T getInstance(String className)
	{
		try
		{
			@SuppressWarnings("unchecked")
			T instance = (T) Class.forName(className).newInstance();
			return instance;
		}
		catch (Exception e)
		{
			throw new GeneralException(e);
		}
	}

	public static <T> T getInstance(String className, Object... parameters)
	{
		if (parameters == null || parameters.length == 0)
		{
			return getInstance(className);
		}

		@SuppressWarnings("rawtypes")
		Class[] parameterClasses = new Class[parameters.length];
		for (int index = 0; index < parameters.length; index++)
		{
			Object parameter = parameters[index];
			parameterClasses[index] = parameter.getClass();
		}

		try
		{
			@SuppressWarnings("unchecked")
			Constructor<T> constructor = (Constructor<T>) Class.forName(
					className).getConstructor(parameterClasses);
			T instance = constructor.newInstance(parameters);

			return instance;
		}
		catch (Exception e)
		{
			throw new GeneralException(e);
		}
	}

	//	public static void main(String[] args)
	//	{
	//		getInstance("edu.uic.cs.cs582.misc.TestClass");
	//		getInstance("edu.uic.cs.cs582.misc.TestClass", 3, 4);
	//	}
}
