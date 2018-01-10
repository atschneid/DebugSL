package data;

import java.util.Arrays;

import misc.GeneralException;

/**
 * Enumeration for the polarity
 * 
 * @author Hong Wang
 *
 */
public enum Polarity
{
	POSITIVE, NEGATIVE, NEUTRAL;

	private static final String SYMBOL_POSITIVE = "Positive";
	private static final String SYMBOL_NEGATIVE = "Negative";
	private static final String SYMBOL_NEUTRAL = "Neutral";

	public static Polarity parsePolarity(String symbol)
	{
		symbol = symbol.trim();

		if (SYMBOL_POSITIVE.equalsIgnoreCase(symbol))
		{
			return POSITIVE;
		}
		else if (SYMBOL_NEGATIVE.equalsIgnoreCase(symbol))
		{
			return NEGATIVE;
		}
		else if (SYMBOL_NEUTRAL.equalsIgnoreCase(symbol))
		{
			return NEUTRAL;
		}
		else
		{
			throw new GeneralException("Can not parse symbol[" + symbol
					+ "] into " + Arrays.toString(Polarity.values()));
		}
	}
}
