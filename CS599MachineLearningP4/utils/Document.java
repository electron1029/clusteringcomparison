package CS599MachineLearningP4.utils;
import java.util.ArrayList;
import java.util.Collections;

public class Document implements Comparable<Document>
{
	// variables
	private ArrayList<Word> tokens;
	private int docid;
	
	/**
	 * Constructor
	 * @param docid
	 */
	public Document(int docid)
	{
		tokens = new ArrayList<Word>();
		this.docid = docid;
	}
	
	/**
	 * public access to get the docid
	 * @return
	 */
	public int getDocid()
	{
		return docid;
	}
	
	/**
	 * add a word to the list of words if it does not
	 * already exist
	 * @param word
	 */
	public void addWord(Word word)
	{
		if (!containsWord(word))
		{
			tokens.add(word);
		}
	}
	
	/**
	 * check if the list of words contains the 
	 * word to be added
	 * @param word
	 * @return
	 */
	public boolean containsWord(Word word)
	{
		return tokens.contains(word);
	}
	
	/**
	 * Return the index of the word at the index
	 * @param i
	 * @return
	 */
	public Word getWordAtIndex(int i)
	{
		return tokens.get(i);
	}
	
	/**
	 *  return the word at the specific index
	 * @param i
	 * @return
	 */
	public String getWordAtIndexAsString(int i)
	{
		return tokens.get(i).getTerm();
	}
	
	/**
	 * Find the ffrequencies of the different words
	 * @param word
	 * @return
	 */
	public int wordFrequency(String word)
	{
		return tokens.get(tokens.indexOf(new Word(word, 0))).getFrequency();
	}
	
	/**
	 * increment the count of a word in the document
	 * @param word
	 */
	public void incrementWordCount(String word)
	{
		tokens.get(tokens.indexOf(new Word(word, 0))).incrementFrequency();
	}
	
	/**
	 * return the doc length
	 * @return
	 */
	public int getDocLength()
	{
		return tokens.size();
	}
	
	/**
	 * return the list of words in the docment
	 * @return
	 */
	public ArrayList<Word> getWordList()
	{
		return tokens;
	}
	
	/**
	 * Sort the list of words alphabetically
	 */
	public void sortList()
	{
		Collections.sort(tokens);
	}

	/**
	 * Compare two documents to see if they are
	 * the same (docids are equal)
	 */
	@Override
	public int compareTo(Document d) 
	{
		if (this.docid == d.docid)
		{
			return 0;
		} else if (this.docid < d.docid)
		{
			return -1;
		} else
		{
			return 1;
		}
	}
	
	/**
	 * Determine if two words are equal
	 */
	public boolean equals(Object o)
	{
		if (o instanceof Document)
		{
			if (this.docid == ((Document) o).docid)
			{
				return true;
			}
			return false;
		}
		
		return false;
	}
	
	/**
	 * A tostring representation of the document
	 */
	public String toString()
	{
		String temp = docid + ":  ";
		for (Word w : this.tokens)
		{
			temp += "(" + w.toString() + ")  ";
		}
		
		return temp;
	}

}
