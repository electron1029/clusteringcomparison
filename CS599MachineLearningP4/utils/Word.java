package CS599MachineLearningP4.utils;

public class Word implements Comparable<Word>
{
	// privte class variables
	private String term;
	private int termFrequency;
	private double tfidf;
	
	/**
	 * Constructor
	 * @param term			a word as a string
	 * @param termFrequency	the frequency of the word
	 */
	public Word(String term, int termFrequency)
	{
		this.term = term;
		this.termFrequency = termFrequency;
	}
	
	/**
	 * public accessor for term
	 * @return
	 */
	public String getTerm()
	{
		return term;
	}
	
	/**
	 * Public accessor for frequency
	 * @return
	 */
	public int getFrequency()
	{
		return termFrequency;
	}
	
	/**
	 * Public function to allow modification of the
	 * frequency field
	 */
	public void incrementFrequency()
	{
		termFrequency++;
	}
	
	/**
	 * Public accessor to allow the setting of the 
	 * tfidf parameter
	 * @param tfidf
	 */
	public void setTfidf(double tfidf)
	{
		this.tfidf = tfidf;
	}
	
	/**
	 * Mathematically calculates tfidf
	 * @param d
	 */
	public void calculateTfidf(double d)
	{
		tfidf = (1 + Math.log10(termFrequency)) * Math.log10(d);
	}
	
	/** 
	 * public access to get the tfidf value
	 * @return
	 */
	public double getTfidf()
	{
		return tfidf;
	}

	/**
	 * Lets us compare two word objects
	 */
	@Override
	public int compareTo(Word w) 
	{
		return this.term.compareTo(w.term);
	}
	
	/**
	 * Alows us to see if two word objects are
	 * equivalent
	 */
	public boolean equals(Object o)
	{
		if (o instanceof Word)
		{
			return this.term.equals(((Word) o).term);
		}
		
		return false;
	}
	
	/**
	 * Allows for word objects to be printed out in a human
	 * readable manner
	 */
	public String toString()
	{
		return "<" + term + "," + termFrequency + "," + tfidf + ">";
	}
}
