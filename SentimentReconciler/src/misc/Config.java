package misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

public class Config
{
	public static final Properties CONFIG = new Properties();
	static
	{
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inStream = classLoader.getResourceAsStream("config.properties");

		try
		{
			CONFIG.load(inStream);
		}
		catch (IOException e)
		{
			throw new GeneralException(e);
		}
	}

	/**
	 * Timeout of the sat4j solver. 
	 * When the timeout is reached, a timeout exception is launched by the solver.
	 */
	public static final long TIMEOUT = Long.valueOf(CONFIG.getProperty("sat_timeout_in_millisecond"));

	/**
	 * sat4j required parameter: 
	 * "Create how many variables in the solver (and thus in the vocabulary)."
	 */
	public static final int MAXVAR = Integer.valueOf(CONFIG.getProperty("sat_max_variable_number"));

	/**
	 * Max synsets number can be processed for each word.
	 * Due to the standard CNF conversion increase exponential in the size, 
	 * large number of majority-synsets may cause {@link OutOfMemoryError}
	 */
	public static final int MAX_SUPPORT_MAJORITY_SYNSETS_NUMBER = Integer.valueOf(CONFIG.getProperty("max_support_majority-synsets_number"));

	/**
	 * In HYBRID method ONLY, 
	 * when synsets number <= synsets_number_threshold_for_hybrid, method ONE is used
	 * when synsets number > synsets_number_threshold_for_hybrid, method TWO is used
	 */
	public static final int SYNSETS_NUMBER_THRESHOLD_FOR_HYBRID = Integer.valueOf(CONFIG.getProperty("synsets_number_threshold_for_hybrid"));

	public static enum Method
	{
		ONE, TWO, HYBRID;
	}

	public static final Method METHOD = Method.valueOf(CONFIG
			.getProperty("method"));

	/**
	 * Where the WordNet dictionary is. 
	 */
	public static final String WORDNET_PATH = CONFIG.getProperty("wordNet_path");

	/**
	 * Where to output the result
	 */
	public static final String OUTPUT_FILE_PATH = CONFIG.getProperty("result_output_file");

	public static final String _IGNORING_WORDS = CONFIG.getProperty("ignoring_words");
	public static final Set<String> _IGNORING_WORDS_SET = new HashSet<String>();
	static
	{
		StringTokenizer tokenizer = new StringTokenizer(_IGNORING_WORDS,
				" \t\n\r\f,");
		while (tokenizer.hasMoreTokens())
		{
			_IGNORING_WORDS_SET.add(tokenizer.nextToken());
		}
	}

	/**
	 * Which word should be ignored;
	 * Because some words have too many synsets, 
	 * that cause too many possible combinations of majority-synsets, we need to ignore them.
	 */
	public static final Set<String> IGNORING_WORDS_SET = Collections.unmodifiableSet(_IGNORING_WORDS_SET);

	public static final String STORE_IN_DISK_PATH = CONFIG.getProperty("store_in_disk_path");

	public static final String _STORE_IN_DISK_WORDS = CONFIG.getProperty("store_in_disk_words");
	public static final Set<String> _STORE_IN_DISK_WORDS_SET = new HashSet<String>();
	static
	{
		StringTokenizer tokenizer = new StringTokenizer(_STORE_IN_DISK_WORDS," \t\n\r\f,");
		while (tokenizer.hasMoreTokens())
		{
			_STORE_IN_DISK_WORDS_SET.add(tokenizer.nextToken());
		}
	}
	public static final Set<String> STORE_IN_DISK_WORDS_SET = Collections.unmodifiableSet(_STORE_IN_DISK_WORDS_SET);

	public static final Float _DEFAULT_MINIMUN_FREQUENCY = Float.valueOf(CONFIG.getProperty("default_minimun_frequency"));
	static
	{
		Assert.isTrue(
				((int) (1 / _DEFAULT_MINIMUN_FREQUENCY.floatValue()))
						* _DEFAULT_MINIMUN_FREQUENCY.floatValue() == 1,
				"[default_minimun_frequency="
						+ _DEFAULT_MINIMUN_FREQUENCY.floatValue()
						+ "] in [config.properties] must < 1, and 1 can divide exactly by this [default_minimun_frequency]. ");
	}
	public static final Float DEFAULT_MINIMUN_FREQUENCY = _DEFAULT_MINIMUN_FREQUENCY;

	public static final int THRESHOLD_FOR_REPLACING_ZERO_WITH_MINIMUN_FREQUENCY = Integer.parseInt(CONFIG.getProperty("threshold_for_replacing_zero_with_minimun_frequency"));

	public static final String LOGIC_FORMULA_PROCESSOR_CLASS_NAME = CONFIG.getProperty("logic_formula_processor_class_name");

	public static final String SAT_PROCESSOR_CLASS_NAME = CONFIG.getProperty("sat_processor_class_name");
}
