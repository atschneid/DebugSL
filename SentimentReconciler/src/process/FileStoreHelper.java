package process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import edu.mit.jwi.item.POS;
import data.Component;
import data.Synset;
import data.Word;
import misc.Assert;
import misc.Config;
import misc.GeneralException;

class FileStoreHelper
{
	private static final String SPLIT_SYMBOL = " ";

	public static boolean hasBeenStored(String word, POS pos)
	{
		File file = new File(Config.STORE_IN_DISK_PATH + File.separator + word
				+ "_" + pos + ".txt");
		return file.exists();
	}

	public static void storeToDisk(Word word, POS pos,
			Collection<int[]> positiveWordExpressionCnfs)
	{
		File file = new File(Config.STORE_IN_DISK_PATH + File.separator
				+ word.getWord() + "_" + pos + ".txt");
		OutputStream out = null;
		try
		{
			out = FileUtils.openOutputStream(file);
			for (int[] clause : positiveWordExpressionCnfs)
			{
				for (int variable : clause)
				{
					String synsetId = mapIndexBackToSynsetId(
							Math.abs(variable / 10),
							word.getComponentBelongsTo());
					IOUtils.write((variable >= 0 ? "+" : "-") + "_" + synsetId
							+ "_" + (variable % 10) + SPLIT_SYMBOL, out);
				}

				IOUtils.write(IOUtils.LINE_SEPARATOR, out);
			}
		}
		catch (IOException e)
		{
			throw new GeneralException(e);
		}
		finally
		{
			IOUtils.closeQuietly(out);
		}

	}

	private static String mapIndexBackToSynsetId(int variable,
			Component component)
	{
		Synset synset = component.getSynsetByIndex(variable);
		return synset.getId();
	}

/*	public static Set<int[]> getCnfFromDisk(Word word, POS pos)
	{
		File file = new File(Config.STORE_IN_DISK_PATH + File.separator
				+ word.getWord() + "_" + pos + ".txt");
		TreeSet<int[]> result = new TreeSet<int[]>(new IntArrayComparator());

		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null)
			{
				String[] variblesInLine = line.split(SPLIT_SYMBOL);
				int[] clause = new int[variblesInLine.length];
				for (int index = 0; index < variblesInLine.length; index++)
				{
					clause[index] = convertDiskCacheBackToVariable(
							variblesInLine[index], word.getComponentBelongsTo());
				}

				result.add(clause);

				line = reader.readLine();
			}

			return result;
		}
		catch (IOException e)
		{
			throw new GeneralException(e);
		}
		finally
		{
			IOUtils.closeQuietly(reader);
		}
	}
*/
	private static int convertDiskCacheBackToVariable(String stringVar,
			Component component)
	{
		String[] parts = stringVar.split("_");
		Assert.isTrue(parts.length == 3);

		String index = component.getIndexOfSynset(parts[1]).toString();

		return Integer.parseInt((parts[0].equals("+") ? "" : "-") + index
				+ parts[2]);
	}
}
