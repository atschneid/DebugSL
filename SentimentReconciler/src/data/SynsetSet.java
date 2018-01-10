package data;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SynsetSet extends HashSet<Synset>
{
	private static final long serialVersionUID = 6758995193590924457L;

	//	private Polarity supposedPolarity = null;

	/*public SynsetSet(Polarity supposedPolarity)
	{
		this.supposedPolarity = supposedPolarity;
	}*/

	@Override
	public int hashCode()
	{
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		for (Synset synset : this)
		{
			hashCodeBuilder.append(synset);
		}

		//hashCodeBuilder.append(supposedPolarity);

		return hashCodeBuilder.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof SynsetSet)
				|| this.size() != ((SynsetSet) obj).size()

		/*|| this.supposedPolarity != ((SynsetSet) obj).supposedPolarity*/)
		{
			return false;
		}

		return this.containsAll((SynsetSet) obj);
	}

	public boolean hasCommon(SynsetSet other)
	{
		SynsetSet small = null;
		SynsetSet large = null;
		if (this.size() <= other.size())
		{
			small = this;
			large = other;
		}
		else
		{
			small = other;
			large = this;
		}

		Iterator<Synset> smallIterator = small.iterator();
		while (smallIterator.hasNext())
		{
			if (large.contains(smallIterator.next()))
			{
				return true;
			}
		}

		return false;
	}

	/*public Polarity getSupposedPolarity()
	{
		return supposedPolarity;
	}*/

}
