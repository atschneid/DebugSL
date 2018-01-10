package process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
//import org.apache.commons.logging.impl.Log4JLogger;
import java.util.logging.Logger;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import data.Component;
import data.Polarity;
import data.Synset;
import data.Word;
import misc.Config;
import misc.GeneralException;
import misc.LogHelper;
//import process.WordsComponentBuilder;

public class WordsComponentBuilderImpl // implements WordsComponentBuilder
{
	private static final Logger LOGGER = LogHelper
			.getLogger(WordsComponentBuilderImpl.class);

	private IDictionary dict = null;

	public WordsComponentBuilderImpl(String wordNetPath)
	{
		URL url = null;
		try
		{
			url = new URL("file", null, wordNetPath);
		}
		catch (MalformedURLException e)
		{
			throw new GeneralException(e);
		}

		dict = new Dictionary(url);
	}

	//@Override
	public List<Component> buildComponents(String filePath, POS pos) throws IOException
	{
		if (!dict.isOpen() && !dict.open())
		{
			throw new GeneralException("Open WordNet Dictionary failed. ");
		}

		Properties properties = loadWordsWithPolarityFromFile(filePath);
		List<Word> words = parsePropertiesIntoWords(properties, pos);
		dict.close();
		
		Collections.sort(words);
		List<Component> result = constructComponents(words, pos);
		return result;
	}

	private Properties loadWordsWithPolarityFromFile(String file)
	{
		Properties wordsWithPolarity = new Properties();

		InputStream inputStream;
		try
		{
			inputStream = FileUtils.openInputStream(new File(file));
			wordsWithPolarity.load(inputStream);
		}
		catch (IOException e)
		{
			throw new GeneralException(
					"Catch IOException while reading words from file[" + file
							+ "]. ", e);
		}
		IOUtils.closeQuietly(inputStream);

		return wordsWithPolarity;
	}

	private List<Word> parsePropertiesIntoWords(Properties properties, POS pos)
	{
		List<Word> wordList = new ArrayList<Word>(properties.size());

		Word word = null;
		String wordString = null;
		Polarity polarity = null;
		for (Entry<Object, Object> entry : properties.entrySet())
		{
			wordString = (String) entry.getKey();
			if (Config.IGNORING_WORDS_SET.contains(wordString))
			{
				/*LOGGER.warn("Ignoring word[" + wordString
						+ "] since it cannot be processed by our PC. ");
				*/
                                continue;
			}

			polarity = Polarity.parsePolarity((String) entry.getValue());

			word = constructAndGetSynsetsBelongTo(wordString, polarity, pos);
			if (word != null)
			{
				wordList.add(word);
			}
		}

		return wordList;
	}

	private Word constructAndGetSynsetsBelongTo(String wordString,
			Polarity polarity, POS pos)
	{
		Word resultWord = new Word(wordString, polarity);

		IIndexWord idxWord = dict.getIndexWord(wordString, pos);
		if (idxWord == null)
		{
			/*LOGGER.warn(wordString + " with " + pos
					+ " does not exist in WordNet! ");
                        */
			return null;
		}

		boolean needReplaceZero = true;
		Map<Synset, Integer> frequenciesBySynset = new HashMap<Synset, Integer>();
		for (IWordID wordID : idxWord.getWordIDs())
		{
			IWord word = dict.getWord(wordID);
			int frequency = dict.getSenseEntry(word.getSenseKey())
					.getTagCount();
			Synset synset = new Synset(word.getSynset());
			frequenciesBySynset.put(synset, frequency);
			if (needReplaceZero && frequency > 0)
			{
				needReplaceZero = false;
			}

		}

		if (frequenciesBySynset.size() <= Config.THRESHOLD_FOR_REPLACING_ZERO_WITH_MINIMUN_FREQUENCY)
		{
			needReplaceZero = true;
		}

		for (Entry<Synset, Integer> entry : frequenciesBySynset.entrySet())
		{
			resultWord.addSynsetRelateTo(entry.getKey(), entry.getValue()
					.intValue(), needReplaceZero);
		}

		return resultWord;
	}

	private List<Component> constructComponents(List<Word> words, POS pos)
	{
		//Eddy: Collections.sort(words);
		Map<Synset, TreeSet<Word>> wordsBySynset = getWordsBySynset(words);

		ArrayList<Component> result = new ArrayList<Component>();

		HashSet<Word> wordsHaveProcessed = new HashSet<Word>(words.size());
		for (Word word : words)
		{
			if (wordsHaveProcessed.contains(word))
			{
				continue;
			}

			Component component = new Component();
			recursiveProcessEachWord(wordsBySynset, wordsHaveProcessed,
					component, word, pos);
			result.add(component);
		}

		Collections.sort(result);
		return result;
	}

	private void recursiveProcessEachWord(
			Map<Synset, TreeSet<Word>> wordsBySynset,
			HashSet<Word> wordsHaveProcessed, Component component, Word word,
			POS pos)
	{
		wordsHaveProcessed.add(word);
		// 2010_01_06 BEGIN ////////////////////////////////////////////////////
		if (Config.STORE_IN_DISK_WORDS_SET.contains(word.getWord()))
		{
			if (FileStoreHelper.hasBeenStored(word.getWord(), pos))
			{
				component.addAlreadyStoredInDiskWord(word);
			}
			else
			{
				component.addToStoreInDiskWord(word);
			}
		}
		component.addWord(word);

		// 2010_01_06 END //////////////////////////////////////////////////////

		for (Synset synset : word.getSynsetsRelateTo())
		{
			for (Word wordOfSynset : wordsBySynset.get(synset))
			{
				if (!wordsHaveProcessed.contains(wordOfSynset))
				{
					recursiveProcessEachWord(wordsBySynset, wordsHaveProcessed,
							component, wordOfSynset, pos);
				}
			}
		}
	}

	private Map<Synset, TreeSet<Word>> getWordsBySynset(List<Word> words)
	{
		Map<Synset, TreeSet<Word>> wordsBySynset = new HashMap<Synset, TreeSet<Word>>();
		TreeSet<Word> wordListForEachSynset = null;
		for (Word word : words)
		{
			for (Synset synset : word.getSynsetsRelateTo())
			{
				wordListForEachSynset = wordsBySynset.get(synset);
				if (wordListForEachSynset == null)
				{
					wordListForEachSynset = new TreeSet<Word>();
					wordsBySynset.put(synset, wordListForEachSynset);
				}

				wordListForEachSynset.add(word);
			}
		}

		return wordsBySynset;
	}
}
