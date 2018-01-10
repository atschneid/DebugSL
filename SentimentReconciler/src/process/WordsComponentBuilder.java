package process;

import java.util.List;

import edu.mit.jwi.item.POS;
import data.Component;

public interface WordsComponentBuilder
{
	/**
	 * Build the Components from the input file
	 * 
	 * @param filePath file's path
	 * @param pos words' part of speech in this file
	 * @return Components for this file
	 */
	List<Component> buildComponents(String filePath, POS pos);
}
