package sentimentreconciler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
//import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
//import org.apache.commons.logging.impl.Log4JLogger;
import java.util.logging.Logger;
//import org.sat4j.specs.TimeoutException;

import edu.mit.jwi.item.POS;
import data.Component;
import data.Synset;
import data.SynsetSet;
import data.Word;

import optimize.Optimize;
//import edu.uic.cs.cs582.misc.Assert;
//import edu.uic.cs.cs582.misc.ClassFactory;
import misc.Config;
import misc.GeneralException;
import misc.LogHelper;
//import edu.uic.cs.cs582.process.LogicFormulaProcessor;
//import edu.uic.cs.cs582.process.SatProcessor;
//import process.WordsComponentBuilder;
//import s582.process.impl.SatIndexParser.Index;
import process.WordsComponentBuilderImpl;
//import edu.uic.edu.cs.cs582.iteration.PolarityInconsistency;

public class SentimentReconciler {

	private static final Logger LOGGER = LogHelper.getLogger(SentimentReconciler.class);

	private static final String INPUT_FOLDER_NAME = "input";

	private static final String INPUT_INFO_FILE = "inputFiles.properties";

	private static OutputStream OUTPUTSTREAM = null;

	public static void main(String[] args)
	{
		try
		{
			preExecute();

			Properties inputFiles = readInputFilesInfomation();
			List<Entry<Object, Object>> inputFilesList = sortFileNames(inputFiles);

			// process each file
			for (Entry<Object, Object> entry : inputFilesList)
			{
				String fileName = entry.getKey().toString();
				POS pos = POS.valueOf(entry.getValue().toString()
						.toUpperCase(Locale.US));

				processOneFileOfCertainPos(fileName, pos);
			}
		}
		finally
		{
			postExecute();
		}
	}

	private static void preExecute()
	{
		File outputFile = new File(Config.OUTPUT_FILE_PATH);
		try
		{
			OUTPUTSTREAM = FileUtils.openOutputStream(outputFile);
		}
		catch (IOException ioe)
		{
			throw new GeneralException(ioe);
		}
	}

	/**
	 * sort files in alphabetic order
	 * 
	 * @param inputFiles
	 * @return
	 */
	private static List<Entry<Object, Object>> sortFileNames(
			Properties inputFiles)
	{
		List<Entry<Object, Object>> inputFilesList = new ArrayList<Entry<Object, Object>>(
				inputFiles.entrySet());
		Collections.sort(inputFilesList,
				new Comparator<Entry<Object, Object>>()
				{
					@Override
					public int compare(Entry<Object, Object> one,
							Entry<Object, Object> other)
					{
						String oneInString = one.getKey().toString()
								+ one.getValue().toString().toUpperCase(Locale.US);
						String otherInString = other.getKey().toString()
								+ other.getValue().toString().toUpperCase(Locale.US);
						return oneInString.compareTo(otherInString);
					}
				});

		return inputFilesList;
	}

	/**
	 * read files' information, 
	 * this file {@link #INPUT_INFO_FILE} contains the files need to check, 
	 * and the words' POS in the file
	 * 
	 * @return
	 */
	private static Properties readInputFilesInfomation()
	{
		Properties inputFiles = new Properties();
		InputStream inStream = ClassLoader
				.getSystemResourceAsStream(INPUT_INFO_FILE);

		try
		{
			inputFiles.load(inStream);
		}
		catch (IOException e)
		{
			throw new GeneralException(e);
		}

		return inputFiles;
	}

	private static void postExecute()
	{
		IOUtils.closeQuietly(OUTPUTSTREAM);
	}

	private static void println(String line)
	{
		System.out.println(line);

		try
		{
			IOUtils.write(line, OUTPUTSTREAM);
			IOUtils.write(IOUtils.LINE_SEPARATOR, OUTPUTSTREAM);
		}
		catch (IOException e)
		{
			throw new GeneralException("Catch IOException while write line to output file. ",	e);
		}
	}

	private static void println()
	{
		println("");
	}

	private static void processOneFileOfCertainPos(String fileName, POS pos)
	{
		println("Input file [" + fileName + "]. ");

		String inputPath = SentimentReconciler.class.getClassLoader().getResource(".")
				.getFile()
				+ File.separator
				+ INPUT_FOLDER_NAME
				+ File.separator
				+ fileName;
                //Andrew : changed to ComponentBuilderImpl
		WordsComponentBuilderImpl componentBuilder = new WordsComponentBuilderImpl(Config.WORDNET_PATH);
		//println("Beginning to build components for [" + pos	+ "], please stand by... ");

		// build the components for each file
		List<Component> components;
                try
                {
                    components = componentBuilder.buildComponents(inputPath, pos);
                }
                catch(IOException e)
                {
                    throw new GeneralException(e);
                }
		println("Finished building components for [" + pos + "]. Total ["
				+ components.size() + "] components. ");
		//println("Beginning to process each component, please stand by... ");
		println();
		//println("=====================================");

		Set<String> allConflictWords = new HashSet<String>();
		Map<Integer, Integer> distribution = new HashMap<Integer, Integer>();

		long beginTime = System.currentTimeMillis();
		//process each componment
                Optimize.solve(components);
		//Component component = null;
		//Eddy 11/22/2013.
//		Set<PolarityInconsistency> allPolarityInconsistenciesInComponent = new HashSet<PolarityInconsistency>(); 
		//for (int componentCount = 1; componentCount <= components.size(); componentCount++)
		//{
		//	component = components.get(componentCount - 1);
			//Eddy 11/22/2013: int beginSize = allConflictWords.size();
//			allPolarityInconsistenciesInComponent.clear();
			//Eddy 11/22/2013: Andrew, in this function will be your code for each component
//			processComponent(pos, componentCount, component, allPolarityInconsistenciesInComponent, false);
			
                        
                  //      Optimize.solve(component);
                        
			//Eduard: this code simply counts the inconsistencies. It can be commented out
//			if(allPolarityInconsistenciesInComponent.isEmpty())
//				continue;
			
//			for(PolarityInconsistency polInconsistency : allPolarityInconsistenciesInComponent)
//			{
//				Integer polInconsistencySize = polInconsistency.getWordCount();
//				Integer countForInconsistencySize = distribution.get(polInconsistencySize);
//				countForInconsistencySize = countForInconsistencySize == null ? 1 : ++countForInconsistencySize;
//				distribution.put(polInconsistencySize, countForInconsistencySize);
//			}
			
		//}

		long endTime = System.currentTimeMillis();
		//println("\nProcessed all components in [" + (endTime - beginTime)
		//		/ 1000 + "] seconds.");

//		println("Distribution(cardinality=number): " + distribution + "\n");
//		for (String wordString : allConflictWords)
//		{
//			println(wordString);
//		}
		//println("\n\n");

		//LOGGER.warn("##################################################################################################");
	}
	
	
	//Eddy: split the function processOneFileOfCertainPos into read and compute
	//Eduard: Andrew, this function reads the data and creates the connected components
	private static List<Component> readDataFromOneFileOfCertainPos(String fileName, POS pos)
	{
		println("Input file [" + fileName + "]. ");

		String inputPath = SentimentReconciler.class.getClassLoader().getResource(".")
				.getFile()
				+ File.separator
				+ INPUT_FOLDER_NAME
				+ File.separator
				+ fileName;

                //Andrew : Changed to Impl
		WordsComponentBuilderImpl componentBuilder = new WordsComponentBuilderImpl(Config.WORDNET_PATH);
		println("Beginning to bulid components for [" + pos	+ "], please stand by... ");

		// build the components for each file
		List<Component> components;
                try
                {
                    components = componentBuilder.buildComponents(inputPath, pos);
                }
                catch(IOException e)
                {
                    throw new GeneralException(e);
                }
		println("Finished buildilg components for [" + pos + "]. Total ["
				+ components.size() + "] components. ");
		println("Beginning to process each component, please stand by... ");
		println();
		//println("=====================================");
		
		return components;
	}

	//Eduard: Andrew, add your computation for a component into this function.
	//Ideally, the code is embedded into a class and here you call the key functions.
	private static void computePolarityInconsistenciesInEachComponent(List<Component> components, POS pos)
	{
		if(components == null || components.isEmpty())
			return;
                
                Optimize.solve(components);
                
		/*
                for(int componentCount = 1; componentCount <= components.size(); componentCount++)
                {
                    Optimize.solve(components.get(componentCount - 1));
                }
                */
/*		Set<String> conflictingWordsInComponent = new HashSet<String>();
		Set<PolarityInconsistency> allPolarityInconistencies = new HashSet<PolarityInconsistency>(); 
		for(int componentCount = 1; componentCount <= components.size(); componentCount++)
		{
			conflictingWordsInComponent.clear();
			Component component = components.get(componentCount - 1);					
			processEachComponent(pos, componentCount, component, conflictingWordsInComponent, false);			
			
			if(!conflictingWordsInComponent.isEmpty())
			{
				PolarityInconsistency polInc = new PolarityInconsistency(conflictingWordsInComponent);
				allPolarityInconistencies.add(polInc);
			}
                }
*/
        }
		
	
	private static void processEachComponent(POS pos, int componentCount,
			Component component, Set<String> allConflictWords)
	{
		
		
	}	

	
	
	private static void fetchAllConflictWordsAndSynsets(
			Map<Synset, Set<Word>> conflictWordsBySynset,
			Map<SynsetSet, Set<Word>> conflictWordsByMajoritySynsets,
			Set<Word> allConflictWords, Set<Synset> allConflictSynsets)
	{
		for (Entry<Synset, Set<Word>> entry : conflictWordsBySynset.entrySet())
		{
			allConflictWords.addAll(entry.getValue());
			allConflictSynsets.add(entry.getKey());
		}

		for (Entry<SynsetSet, Set<Word>> entry : conflictWordsByMajoritySynsets
				.entrySet())
		{
			allConflictWords.addAll(entry.getValue());
			allConflictSynsets.addAll(entry.getKey());
		}
	}

/*	private static void printOutInconsistenciesForEachComponent(
			Set<String> allConflictWords, SatProcessor satProcessor)
	{
		Map<SynsetSet, Set<Word>> conflictWordsByMajoritySynsets = satProcessor
				.getConflictWordsBySynsetsInMajority();
		for (Map.Entry<SynsetSet, Set<Word>> entry : conflictWordsByMajoritySynsets
				.entrySet())
		{
			String each = ">>>>>>>>* ";
			for (Word word : entry.getValue())
			{
				each += word.getWord() + "[" + word.getPolarity() + "] ";
				allConflictWords.add(word.getWord());
			}
			println(each + " -- " + entry.getKey());
		}

		///////////////////
		Map<Synset, Set<Word>> conflictWordsBySynset = satProcessor
				.getConflictWordsBySynset();
		for (Map.Entry<Synset, Set<Word>> entry : conflictWordsBySynset
				.entrySet())
		{
			String each = ">>>>>>>>  ";
			boolean hasWordsToDisplay = false;
			for (Word word : entry.getValue())
			{
				if (!allConflictWords.contains(word.getWord()))
				{
					each += word.getWord() + "[" + word.getPolarity() + "] ";
					hasWordsToDisplay = allConflictWords.add(word.getWord());
				}
			}

			if (hasWordsToDisplay)
			{
				println(each + " -- (" + entry.getKey() + ") ");
			}
		}
		
	}
*/	
	//Eddy: 11/22/2013
	//Add a function that finds only the inconsistent words from a MUS (minimal unsatisfiable core)
/*	private static void printOutInconsistentWordsForComponent(Set<Word> inconsistentWords, SatProcessor satProcessor)
	{
		TreeSet<Integer> inconsistentWordIndices = satProcessor.getConflictWordIndices();		
		
		if(inconsistentWords == null || inconsistentWordIndices.isEmpty())
		{
			println("No inconsistent word here! There must be a bug. Fix it!");
			return;
		}
		
		String each = ">>>>>>>>* ";
		for (Word word : inconsistentWords)
		{			
			each += word.getWord() + "[" + word.getPolarity() + "] ";					
		}		
		println(each + " -- ");
	}
*/
}