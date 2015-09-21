package CS599MachineLearningP4.utils;

public class Tuple<K extends Comparable<K>, V extends Comparable<V>> implements Comparable<Tuple<K, V>> 
{
	// data for a score entry
	K k;
	V v;

	/**
	 * Constructor sets the initial values.
	 */
	public Tuple(K k, V v)
	{
		this.k = k;
		this.v = v;
	}

	/**
	 * Constructor with no arguments
	 */
	public Tuple() 
	{
	}

	/**
	 * Set k
	 */
	public void setK(K k)
	{
		this.k = k;
	}
	
	/**
	 * get k
	 */
	public K getK()
	{
		return k;
	}
	
	/**
	 * get v
	 */
	public V getV()
	{
		return v;
	}

	/**
	 * Set v
	 */
	public void setV(V v)
	{
		this.v = v;
	}

	/**
	 * CompareTo function allows Collections.sort to sort
	 * the score entries while in an array or list in order
	 * by score
	 * Also allows for simple comparisons of score entry instances
	 * for any other need.
	 */
	@Override
	 public int compareTo(Tuple<K, V> t) 
	{

        int compare = this.getK().compareTo(t.getK());
    
        if (compare == 0)
        {
            compare = this.getV().compareTo(t.getV());
        }
        
        return compare;
    }

	/**
	 * the .equals function allows us to look in the array list
	 * and see if the entry is there. It compares two score entries
	 * to see if they represent the same thing. In this case, the same
	 * "thing" would be the same document.
	 */
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Tuple<?,?>))					// if the second object isn't an ScoreEntry, it's not equal
		{
			return false;
		} else if (k.equals(((Tuple<K,V>)o).k))	// otherwise compare the two docids for equality
		{
			return true;											// if they are equal, then the objects pass the test
		} else
		{
			return false;											// in any other case they are not equal
		}
	}
}
