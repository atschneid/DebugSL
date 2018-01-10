package data;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import misc.Assert;

/**
 * Component for those words related by common synsets. 
 * In other words, if two words share one common synset, they are considered in 
 * the same component 
 * 
 * @author Hong Wang
 *
 */
public class Component implements Comparable<Component>
{
	private Map<Polarity, Set<Word>> wordSetsByPolarity = null;
	private HashSet<Word> allWords = null;
	private HashMap<Integer, Word> wordsByIndex = null;
	private HashMap<String, Integer> indiecsByWordString = null;

	private HashSet<Synset> allSynsetsContains = null;
	private HashSet<Synset> allMajoritySynsetsContains = null;

	private HashMap<String, Integer> indiecsBySynsetId = null;
	private HashMap<Integer, Synset> synsetsByIndex = null;

	private HashSet<Word> alreadyStoredInDiskWords = null;
	private HashSet<Word> toStoreInDiskWords = null;

	private HashMap<Integer, Integer> synsetIndicesByItsFreuqencySymbol = null;

	private int maxSynsetsNumberOneWordRelatesTo = 0;

	public Component()
	{
		Set<Word> positiveWordSet = new TreeSet<Word>();
		Set<Word> negativeWordSet = new TreeSet<Word>();
		Set<Word> neutralWordSet = new TreeSet<Word>();
		wordSetsByPolarity = new HashMap<Polarity, Set<Word>>(3);
		allWords = new HashSet<Word>();

		wordSetsByPolarity.put(Polarity.POSITIVE, positiveWordSet);
		wordSetsByPolarity.put(Polarity.NEGATIVE, negativeWordSet);
		wordSetsByPolarity.put(Polarity.NEUTRAL, neutralWordSet);

		allSynsetsContains = new HashSet<Synset>();
		allMajoritySynsetsContains = new HashSet<Synset>();
	}

	/**
	 * Add a word into this component
	 * @param word
	 */
	public void addWord(Word word)
	{
		wordSetsByPolarity.get(word.getPolarity()).add(word);
		allWords.add(word);

		allSynsetsContains.addAll(word.getSynsetsRelateTo());
		for (Set<Synset> synsets : word.getMajoritySynsets())
		{
			allMajoritySynsetsContains.addAll(synsets);
		}

		// set itself to Word
		word.setComponentBelongTo(this);

		if (maxSynsetsNumberOneWordRelatesTo < word.getSynsetsNumberRelateTo())
		{
			maxSynsetsNumberOneWordRelatesTo = word.getSynsetsNumberRelateTo();
		}

		// make sure after add any new word into this component, the indexes are invalid
		invalidateIndices();
	}

	private void invalidateIndices()
	{
		indiecsBySynsetId = null;
		synsetsByIndex = null;

		wordsByIndex = null;
	}

	/**
	 * get words exist in this component for given polarity
	 * @param polarity
	 * @return
	 */
	public Set<Word> getWordsInComponent(Polarity polarity)
	{
		Assert.notNull(polarity);
		return Collections.unmodifiableSet(wordSetsByPolarity.get(polarity));
	}

	@Override
	public String toString()
	{
		return wordSetsByPolarity.toString();
	}

	//	/**
	//	 * get all majority-synsets exist in this component
	//	 * @return
	//	 */
	//	private HashSet<Synset> getAllMajoritySynsetsContains()
	//	{
	//		return allMajoritySynsetsContains;
	//	}

	public Set<Integer> getAllMajoritySynsetIndices()
	{
		Assert.isTrue(allMajoritySynsetsContains.size() != 0);

		TreeSet<Integer> result = new TreeSet<Integer>();
		for (Synset majoritySynset : allMajoritySynsetsContains)
		{
			Integer index = indiecsBySynsetId.get(majoritySynset.getId());
			Assert.notNull(index);
			result.add(index);
		}

		return result;
	}

	/**
	 * get all words exist in this component
	 * @return
	 */
	public Set<Word> getAllWords()
	{
		return allWords;
	}
	
	public Set<Synset> getAllSynsets()
	{
		return allSynsetsContains;
	}

	public int getWordsNumberBinaryLength()
	{
		return Integer.toBinaryString(getWordsNumber()).length();
	}

	public int getMaxSynsetsNumberBinaryLengthOfOneWord()
	{
		return Integer.toBinaryString(maxSynsetsNumberOneWordRelatesTo)
				.length();
	}

	public int getWordsNumber()
	{
		return getAllWords().size();
	}

	public int getSynsetsNumber()
	{
		return allSynsetsContains.size();
	}
	
	public Word getWordByIndex(Integer index)
	{
		if (wordsByIndex == null || indiecsByWordString == null)
		{
			initialWordIndexes();
		}

		Word result = wordsByIndex.get(index);
		Assert.notNull(result);
		return result;
	}
	
	//Eddy 11/22/2013
	public Set<Word> getAllWordsByIndices(Set<Integer> wordIndices)
	{
		if(wordIndices == null || wordIndices.isEmpty())
			return null;
		
		Set<Word> wordSet = new HashSet<Word>();
		for(Integer index : wordIndices)
		{
			Word word = getWordByIndex(index);
			wordSet.add(word);
		}
		
		return wordSet;
	}

	public Integer getIndexOfWord(Word word)
	{
		return getIndexOfWord(word.getWord());
	}

	public Integer getIndexOfWord(String word)
	{
		if (wordsByIndex == null || indiecsByWordString == null)
		{
			initialWordIndexes();
		}

		Integer result = indiecsByWordString.get(word);
		Assert.notNull(result);
		return result;
	}

	public Integer getIndexOfSynset(Synset synset)
	{
		return getIndexOfSynset(synset.getId());
	}

	public Integer getIndexOfSynset(String synsetID)
	{
		if (indiecsBySynsetId == null || synsetsByIndex == null)
		{
			initialSynsetIndexes();
		}

		Integer result = indiecsBySynsetId.get(synsetID);
		Assert.notNull(result);
		return result;
	}

	public Synset getSynsetByIndex(Integer index)
	{
		if (indiecsBySynsetId == null || synsetsByIndex == null)
		{
			initialSynsetIndexes();
		}

		Synset result = synsetsByIndex.get(index);
		Assert.notNull(result);
		return result;
	}

	private void initialWordIndexes()
	{
		wordsByIndex = new HashMap<Integer, Word>();
		indiecsByWordString = new HashMap<String, Integer>();
		int index = 0;
		for (Word word : allWords)
		{
			++index;
			wordsByIndex.put(index, word);
			indiecsByWordString.put(word.getWord(), index);
		}
	}

	private void initialSynsetIndexes()
	{
		indiecsBySynsetId = new HashMap<String, Integer>();
		synsetsByIndex = new HashMap<Integer, Synset>();
		int index = 0;
		for (Synset synset : allSynsetsContains)
		{
			++index;
			indiecsBySynsetId.put(synset.getId(), index);
			synsetsByIndex.put(index, synset);
		}
	}

	@Override
	public int compareTo(Component other)
	{
		return this.getSynsetsNumber() - other.getSynsetsNumber();
	}

	// 2010_01_06 BEGIN ////////////////////////////////////////////////////////
	public void addAlreadyStoredInDiskWord(Word word)
	{
		if (alreadyStoredInDiskWords == null)
		{
			alreadyStoredInDiskWords = new HashSet<Word>();
		}

		alreadyStoredInDiskWords.add(word);
	}

	public void addToStoreInDiskWord(Word word)
	{
		if (toStoreInDiskWords == null)
		{
			toStoreInDiskWords = new HashSet<Word>();
		}

		toStoreInDiskWords.add(word);
	}

	public boolean alreadyStoredInDisk(Word word)
	{
		if (alreadyStoredInDiskWords == null)
		{
			return false;
		}

		return alreadyStoredInDiskWords.contains(word);
	}

	public boolean willStoreInDiskWords(Word word)
	{
		if (toStoreInDiskWords == null)
		{
			return false;
		}

		return toStoreInDiskWords.contains(word);
	}

	// 2010_01_06 END //////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public Component clone()
	{
		return clone(Collections.EMPTY_SET);
	}

	public Component clone(Set<Word> wordsToRemove)
	{
		Component result = new Component();
		for (Word eachWord : allWords)
		{
			if (!wordsToRemove.contains(eachWord))
			{
				result.addWord(eachWord);
			}
		}

		if (alreadyStoredInDiskWords != null)
		{
			for (Word eachWord : alreadyStoredInDiskWords)
			{
				if (!wordsToRemove.contains(eachWord))
				{
					result.addAlreadyStoredInDiskWord(eachWord);
				}
			}
		}

		if (toStoreInDiskWords != null)
		{
			for (Word eachWord : toStoreInDiskWords)
			{
				if (!wordsToRemove.contains(eachWord))
				{
					result.addToStoreInDiskWord(eachWord);
				}
			}
		}

		return result;
	}
	
	//Eddy 11/20/2013: I added this method to remove a set of words from a component.
	public void removeWords(Set<Word> wordsToRemove)
	{	
		if(wordsToRemove == null || allWords == null)
			return;
		
		allWords.removeAll(wordsToRemove);		

		if (alreadyStoredInDiskWords != null)
			alreadyStoredInDiskWords.removeAll(wordsToRemove);
		

		if (toStoreInDiskWords != null)
			toStoreInDiskWords.removeAll(wordsToRemove);
			
	}

	//Eddy 11/20/2013: I added this method to remove a word from a component.
	public void removeWords(Word word)
	{	
		if(word == null || allWords == null)
			return;
		
		allWords.remove(word);		

		if (alreadyStoredInDiskWords != null)
			alreadyStoredInDiskWords.remove(word);
		

		if (toStoreInDiskWords != null)
			toStoreInDiskWords.remove(word);
			
	}
		
	public void addRelationBetweenFrequencySymbolAndSynset(
			Integer frequencySymbol, Integer synsetIndex)
	{
		if (synsetIndicesByItsFreuqencySymbol == null)
		{
			synsetIndicesByItsFreuqencySymbol = new HashMap<Integer, Integer>();
		}

		synsetIndicesByItsFreuqencySymbol.put(frequencySymbol, synsetIndex);
	}

	public Integer getSynsetIndexByFrequencySymbol(Integer frequencySymbol)
	{
		Assert.notNull(synsetIndicesByItsFreuqencySymbol,
				"synsetIndicesByItsFreuqencySymbol has not been initialized. ");

		return synsetIndicesByItsFreuqencySymbol.get(frequencySymbol);
	}
}
