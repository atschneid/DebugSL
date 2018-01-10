package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import misc.Assert;
import misc.Config;

/**
 * Word,
 * each word has a set of synsets; 
 * and this synsets can form a list of majority-synsets
 * 
 * @author Hong Wang
 *
 */
public class Word implements Comparable<Word>
{
	private static final int MINIMUN_FREQUENCY = 1;
	private static final Integer FREQUENCY_EXPANDING_FACTOR = ((int) (MINIMUN_FREQUENCY / Config.DEFAULT_MINIMUN_FREQUENCY
			.floatValue()));

	private static class FrequencyWithSynset
	{
		private Integer frequency;
		private Synset synset;

		public FrequencyWithSynset(Integer frequency, Synset synset)
		{
			this.frequency = frequency;
			this.synset = synset;
		}

		public String toString()
		{
			return frequency + " : " + synset;
		}

	}

	// String form
	private String word = null;

	// the polarity specified by input files
	private Polarity polarity = null;

	// frequency for each synset in this word
	private Map<Synset, Integer> frequenciesBySynset = null;
	private Map<Synset, Integer> indeicesInWordBySynset = null;
	private Map<Integer, Synset> synsetsByIndexInWord = null;

	private int frequencySum = 0;

	// the list storing the majority-synsets
	private List<SynsetSet> majoritySynsets = null;
	private Set<Synset> allSynsetsInMajoritySynsets = null;

	private Component componentBelongTo = null;

	private int totalFrequencyDigitNumber = -1;

	public Word(String word, Polarity polarity)
	{
		this.word = word;
		this.polarity = polarity;
		this.frequenciesBySynset = new HashMap<Synset, Integer>()
		{
			private static final long serialVersionUID = -1789017091521299011L;

			@Override
			public String toString()
			{
				Iterator<Entry<Synset, Integer>> i = entrySet().iterator();
				if (!i.hasNext())
				{
					return "{}";
				}

				StringBuilder sb = new StringBuilder();
				sb.append('{');
				for (;;)
				{
					Entry<Synset, Integer> e = i.next();
					Synset key = e.getKey();
					Integer value = e.getValue();
					sb.append(key);
					sb.append('=');
					sb.append(value.floatValue() / FREQUENCY_EXPANDING_FACTOR);
					if (!i.hasNext())
					{
						return sb.append('}').toString();
					}
					sb.append(", ");
				}
			}

		};

		this.indeicesInWordBySynset = new HashMap<Synset, Integer>();
		this.synsetsByIndexInWord = new HashMap<Integer, Synset>();
	}

	/**
	 * Add one related synset into this word.<br>
	 * If one word only has synsets whose frequency is 0, 
	 * then needReplaceZero will be true
	 * 
	 * 
	 * @param synset
	 * @param frequency
	 * @param needReplaceZero if needReplaceZero is true, and the frequency is 0,
	 * 		then the frequency is replaced with {@link #Config.DEFAULT_MINIMUN_FREQUENCY}
	 */
	public void addSynsetRelateTo(Synset synset, int frequency,
			boolean needReplaceZero)
	{
		Assert.isTrue(frequency >= 0, "Synset[" + synset + "]'s frequency["
				+ frequency + "] should >= 0. ");

		Integer frequencyInInt = null;
		if (frequency == 0 && needReplaceZero)
		{
			frequencyInInt = MINIMUN_FREQUENCY;
		}
		else
		{
			frequencyInInt = Integer.valueOf(FREQUENCY_EXPANDING_FACTOR
					* frequency);
		}
		/*if ("strictly".equals(word))
		{
			if (synset.toString().equals("SID-00469302-R"))
			{
				frequencyInInt = 1;
			}
			else if (synset.toString().equals("SID-00225410-R"))
			{
				frequencyInInt = 1;return;
			}
			else if (synset.toString().equals("SID-00179112-R"))
			{
				frequencyInInt = 2;
			}
			else
			{
				throw new GeneralException();
			}

		}
		else if ("stringently".equals(word))
		{
			if (synset.toString().equals("SID-00469302-R"))
			{
				frequencyInInt = 1;
			}
			else
			{
				throw new GeneralException();
			}
		}
		else if ("purely".equals(word))
		{
			if (synset.toString().equals("SID-00179112-R"))
			{
				frequencyInInt = 1;
			}
			else
			{
				throw new GeneralException();
			}
		}
		else
		{
			throw new GeneralException();
		}*/

		if (!frequenciesBySynset.containsKey(synset))
		{
			frequenciesBySynset.put(synset, frequencyInInt);
			frequencySum += frequencyInInt;

			indeicesInWordBySynset.put(synset,
					indeicesInWordBySynset.size() + 1);
			synsetsByIndexInWord.put(synsetsByIndexInWord.size() + 1, synset);
		}

		// make sure majoritySynsets will be re-calculated when any synset is added in
		invalidateMajoritySynsets();
	}

	private void invalidateMajoritySynsets()
	{
		majoritySynsets = null;
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(word).hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean equals = false;

		if (obj instanceof Word)
		{
			Word other = (Word) obj;
			equals = new EqualsBuilder().append(word, other.word).isEquals();
		}

		return equals;
	}

	@Override
	public int compareTo(Word other)
	{
		return word.compareTo(other.word);
	}

	public Polarity getPolarity()
	{
		return polarity;
	}

	public Synset getSynsetsByIndexInWord(int index)
	{
		return synsetsByIndexInWord.get(index);
	}

	public int getIndexInWordOfSynset(Synset synset)
	{
		return indeicesInWordBySynset.get(synset);
	}

	/**
	 * get all synsets this word has
	 * @return
	 */
	public Set<Synset> getSynsetsRelateTo()
	{
		return Collections.unmodifiableSet(frequenciesBySynset.keySet());
	}

	public Map<Synset, Integer> getFrequenciesBySynsetRelateTo()
	{
		return Collections.unmodifiableMap(frequenciesBySynset);
	}

	@Override
	public String toString()
	{
		return word + "_" + frequenciesBySynset;
	}

	public String getWord()
	{
		return word;
	}

	public int getTotalFrequencyDigitNumber()
	{
		if (totalFrequencyDigitNumber == -1)
		{
			totalFrequencyDigitNumber = 0;
			for (Integer frequency : getFrequenciesBySynsetRelateTo().values())
			{
				totalFrequencyDigitNumber += (Integer.toBinaryString(frequency)
						.length());
			}
		}

		return totalFrequencyDigitNumber;
	}

	/**
	 * get all majority-synsets this word has
	 * 
	 * - Sorted S by its frequency in descending order
	 * - currentIndex = 0, M = {}
	 * MajoritySynsetSelection(SOR, LB, M, F, S, currentIndex)
	 * {
	 * 		if(SOR < LB) return;
	 * 		for i from currentIndex to F.size()
	 *    	{
	 *  		put S[i] into M
	 * 			if (F[i] > LB) 
	 * 				then M is a majority synset
	 * 			else
	 * 				SOR  = sum of all synsets’ frequencies after currentIndex
	 * 				LB = LB - F[i];
	 * 				MajoritySynsetSelection(SOR, LB, M, F, S, (i + 1))
	 * 		}
	 * }
	 * 
	 * @return
	 */
	public List<SynsetSet> getMajoritySynsets()
	{
		if (majoritySynsets != null)
		{
			return majoritySynsets;
		}

		float lowerBound = frequencySum / 2;
		List<FrequencyWithSynset> sortedSynsetListByFrequency = getSortedSynsetByFrequency();

		majoritySynsets = new ArrayList<SynsetSet>();
		SynsetSet lastSynsetBag = new SynsetSet(/*this.polarity*/);

		recursiveGetMajoritySynsets(frequencySum, lowerBound, lastSynsetBag,
				sortedSynsetListByFrequency, 0, majoritySynsets);

		return majoritySynsets;
	}

	public Set<Synset> getAllSynsetsInMajoritySynsets()
	{
		if (allSynsetsInMajoritySynsets != null)
		{
			return allSynsetsInMajoritySynsets;
		}

		List<SynsetSet> allMajoritySynset = getMajoritySynsets();
		allSynsetsInMajoritySynsets = new HashSet<Synset>();
		for (SynsetSet majoritySynsets : allMajoritySynset)
		{
			allSynsetsInMajoritySynsets.addAll(majoritySynsets);
		}

		return allSynsetsInMajoritySynsets;
	}

	private void recursiveGetMajoritySynsets(float sumOfRemains,
			float lowerBound, SynsetSet lastSynsetBag,
			List<FrequencyWithSynset> sortedSynsetListByFrequency,
			int currentIndex, List<SynsetSet> result)
	{
		if (sumOfRemains < lowerBound)
		{
			return;
		}

		//		HashSet<Synset> currentSynsetBag = null;
		for (int index = currentIndex; index < sortedSynsetListByFrequency
				.size(); index++)
		{
			//			System.out.println(lastSynsetBag);
			//			currentSynsetBag = (HashSet<Synset>) lastSynsetBag.clone();
			//			currentSynsetBag.add(sortedSynsetListByFrequency.get(index).synset);
			lastSynsetBag.add(sortedSynsetListByFrequency.get(index).synset);
			if (sortedSynsetListByFrequency.get(index).frequency > lowerBound)
			{
				result.add((SynsetSet) lastSynsetBag.clone());
			}
			else
			{
				float newSumOfRemains = countSumOfRemains(index,
						sortedSynsetListByFrequency);
				float newlowerBound = (lowerBound - sortedSynsetListByFrequency
						.get(index).frequency);
				recursiveGetMajoritySynsets(newSumOfRemains, newlowerBound,
						lastSynsetBag, sortedSynsetListByFrequency, index + 1,
						result);
			}
			lastSynsetBag.remove(sortedSynsetListByFrequency.get(index).synset);
		}
	}

	private float countSumOfRemains(int currentIndex,
			List<FrequencyWithSynset> sortedSynsetListByFrequency)
	{
		float result = 0f;
		for (int index = currentIndex + 1; index < sortedSynsetListByFrequency
				.size(); index++)
		{
			result += sortedSynsetListByFrequency.get(index).frequency;
		}

		return result;
	}

	/**
	 * sort all synsets the word has by reverse-order of frequency
	 * @return
	 */
	private List<FrequencyWithSynset> getSortedSynsetByFrequency()
	{
		List<FrequencyWithSynset> frequencyWithSynsetList = new ArrayList<FrequencyWithSynset>(
				frequenciesBySynset.size());
		for (Entry<Synset, Integer> entry : frequenciesBySynset.entrySet())
		{
			if (entry.getValue().intValue() != 0)
			{
				frequencyWithSynsetList.add(new FrequencyWithSynset(entry
						.getValue(), entry.getKey()));
			}
		}

		Comparator<FrequencyWithSynset> comparator = new Comparator<FrequencyWithSynset>()
		{
			@Override
			public int compare(FrequencyWithSynset o1, FrequencyWithSynset o2)
			{
				return o2.frequency.compareTo(o1.frequency);
			}
		};
		Collections.sort(frequencyWithSynsetList, comparator);

		return frequencyWithSynsetList;
	}

	public void setComponentBelongTo(Component componentBelongTo)
	{
		this.componentBelongTo = componentBelongTo;
	}

	public Component getComponentBelongsTo()
	{
		return componentBelongTo;
	}

	public boolean isAlreadyStoredInDisk()
	{
		return componentBelongTo.alreadyStoredInDisk(this);
	}

	public boolean willStoreInDisk()
	{
		return componentBelongTo.willStoreInDiskWords(this);
	}

	public int getFrequencySum()
	{
		return frequencySum;
	}

	public int getSynsetsNumberRelateTo()
	{
		return getSynsetsRelateTo().size();
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	/*@SuppressWarnings("unchecked")
	private static void test(float sumOfLeft, float lowerBound,
			ArrayList<Integer> lastBag, List<Integer> sortedFrequency,
			int currentIndex, List<ArrayList<Integer>> result)
	{
		//		ArrayList<Integer> currentBag = null;
		for (int index = currentIndex; index < sortedFrequency.size(); index++)
		{
			if (sumOfLeft < lowerBound)
			{
				break;
			}

			//			System.out.println(lastBag);
			//			currentBag = (ArrayList<Integer>) lastBag.clone();
			//			currentBag.add(sortedFrequency.get(index));
			lastBag.add(sortedFrequency.get(index));
			if (sortedFrequency.get(index) > lowerBound)
			{
				result.add((ArrayList<Integer>) lastBag.clone());
			}
			else
			{
				float newSumOfLeft = 0f;
				for (int i = index + 1; i < sortedFrequency.size(); i++)
				{
					newSumOfLeft += sortedFrequency.get(i);
				}

				float newlowerBound = (lowerBound - sortedFrequency.get(index));
				test(newSumOfLeft, newlowerBound, lastBag, sortedFrequency,
						index + 1, result);
			}
			lastBag.remove(sortedFrequency.get(index));
		}
	}

	public static void main(String[] args)
	{
		List<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> lastBag = new ArrayList<Integer>();
		List<Integer> sortedFrequency = null;
		//		sortedFrequency = Arrays.asList(new Integer[] { 10, 7, 5, 4, 3, 2 });
		//
		//		test(31f, 15.5f, lastBag, sortedFrequency, 0, result);
		//		System.out.println(result);
		//		////////////////////////////////////////////////////////////////////////
		//
		//		result = new ArrayList<ArrayList<Integer>>();
		//		lastBag = new ArrayList<Integer>();
		//		sortedFrequency = Arrays.asList(new Integer[] { 10, 7, 5, 4, 3, 2 });
		//
		//		test(31f, 8f, lastBag, sortedFrequency, 0, result);
		//		System.out.println(result);
		//		////////////////////////////////////////////////////////////////////////
		//
		//		result = new ArrayList<ArrayList<Integer>>();
		//		lastBag = new ArrayList<Integer>();
		//		sortedFrequency = Arrays.asList(new Integer[] { 4, 3, 2, 1 });
		//
		//		test(10, 5, lastBag, sortedFrequency, 0, result);
		//		System.out.println(result);
		//		////////////////////////////////////////////////////////////////////////
		//
		//		result = new ArrayList<ArrayList<Integer>>();
		//		lastBag = new ArrayList<Integer>();
		//		sortedFrequency = Arrays.asList(new Integer[] { 10, 7, 5, 2 });
		//
		//		test(24, 12, lastBag, sortedFrequency, 0, result);
		//		System.out.println(result);
		//		////////////////////////////////////////////////////////////////////////
		//
		//		result = new ArrayList<ArrayList<Integer>>();
		//		lastBag = new ArrayList<Integer>();
		//		sortedFrequency = Arrays.asList(new Integer[] { 1, 1 });
		//
		//		test(2, 1, lastBag, sortedFrequency, 0, result);
		//		System.out.println(result);
		//		////////////////////////////////////////////////////////////////////////
		//
		//		result = new ArrayList<ArrayList<Integer>>();
		//		lastBag = new ArrayList<Integer>();
		//		sortedFrequency = Arrays.asList(new Integer[] { 10, 1, 1, 1, 1, 1 });
		//
		//		test(15, 7.5f, lastBag, sortedFrequency, 0, result);
		//		System.out.println(result);
		//		////////////////////////////////////////////////////////////////////////

		result = new ArrayList<ArrayList<Integer>>();
		lastBag = new ArrayList<Integer>();
		sortedFrequency = Arrays.asList(new Integer[] { 1, 1, 1, 1, 1 });

		int sum = 0;
		for (int i : sortedFrequency)
		{
			sum += i;
		}
		test(sum, sum / 2, lastBag, sortedFrequency, 0, result);
		System.out.println(result.size());
		for (ArrayList<Integer> one : result)
		{
			System.out.println(one);
		}
		////////////////////////////////////////////////////////////////////////
	}*/
}
