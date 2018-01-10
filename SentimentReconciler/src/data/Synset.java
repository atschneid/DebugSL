package data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import edu.mit.jwi.item.ISynset;

public class Synset
{
	//	private ISynsetID id = null;
	private String id = null;
	private String gloss = null;

	public Synset(ISynset synset)
	{
		this.id = synset.getID().toString();
		this.gloss = synset.getGloss();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(id).hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean equals = false;

		if (obj instanceof Synset)
		{
			Synset synset = (Synset) obj;
			equals = new EqualsBuilder().append(id, synset.id).isEquals();
		}

		return equals;
	}

	public String getGloss()
	{
		return gloss;
	}

	public String getId()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return id;
	}
}
