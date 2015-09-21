package CS599MachineLearningP4.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import org.tartarus.snowball.ext.englishStemmer;

public class FileParser 
{
	// private class variables
	private ArrayList<Document> data;
	private HashMap<String, Integer> termDocumentOccurances;
	private HashMap<Integer, String> originalSentences;
	private HashMap<Integer, String> labels;
	private String stopWordFile;

	/**
	 * Constructor
	 * Initializes all data structures
	 */
	public FileParser()
	{
		data = new ArrayList<Document>();
		termDocumentOccurances = new HashMap<String, Integer>();
		originalSentences = new HashMap<Integer, String>();
		labels = new HashMap<Integer, String>();
		stopWordFile = "stopWords.txt"; // stop word list
	}
	
	/**
	 *  public accessor for data field
	 * @return
	 */
	public ArrayList<Document> getData()
	{
		return data;
	}
	
	/**
	 * public accessor for termdocumentOccurances field
	 * @return
	 */
	public HashMap<String,Integer> getTermDocumentOccurances()
	{
		return termDocumentOccurances;
	}
	
	/**
	 * public accessor for orginalsentences field
	 * @return
	 */
	public HashMap<Integer,String> getOriginalSentences()
	{
		return originalSentences;
	}
	
	/**
	 * public accessor for labels field
	 * @return
	 */
	public HashMap<Integer,String> getLabels()
	{
		return labels;
	}

	/**
	 * Main file parser.
	 * Reads through the CSV data file and parses the data and class labels
	 * into the proper arrays. Calls functions for stemming and stop word 
	 * filtering. Adds words to a data structure.
	 * @throws Exception
	 */
	public void build() throws Exception 
	{
		File filepath = new File("./data/");						// create a reference to the filepath
		File files[] = filepath.listFiles();					// get a list of all the file names in the specified path
		ArrayList<String> tokens = new ArrayList<String>();		// will hold the tokens from a file for processing

		// iterate through the files in dir, getting the tokens from each
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].isFile())
			{
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(files[i]), "UTF8"));

				// ignore this apple specific file
				if (files[i].getName().equals(".DS_Store"))
				{
					continue;
				}
						
				// parse the line into tokens
				while((line = br.readLine()) != null)
				{			
					String[] splitLine = line.split("\",\"");
					
					originalSentences.put(data.size(), splitLine[1]);
					labels.put(data.size(), splitLine[0]);
					
					tokens = tokenizeADoc(splitLine[1], data.size());	// call method to grab tokens in the current file

					Document d = new Document(data.size());

					// iterate through the returned tokens and add them to the hashmap
					for (int j = 0; j < tokens.size(); j++)
					{
						// for populating the document list
						// get word at jth position in tokens
						String w = tokens.get(j);

						if (d.containsWord(new Word(w, 0)))
						{
							d.incrementWordCount(w);
						} else
						{
							// for recording term frequency
							// first check that the word is not already in the hash map
							if (!termDocumentOccurances.containsKey(w))
							{
								termDocumentOccurances.put(w, 1);
							} else
							{	// word is in the hash map
								Integer currIdf = termDocumentOccurances.get(w);
								termDocumentOccurances.remove(w);
								termDocumentOccurances.put(w, ++currIdf);
							}

							d.addWord(new Word(w, 1));
						}
					}
					data.add(d);
				}
			}
		}

		// find the tfidf values and then sort the lists alphabetically
		cleanUp();
	}

	/**
	 * Adds a single doc to the data set at a time.
	 * @param s
	 */
	public void addSingleDoc(String s)
	{
		originalSentences.put(data.size(), s);
		String[] tokens = s.split(" ");
		Document d = new Document(data.size());

		// iterate through the returned tokens and add them to the hashmap
		for (int j = 0; j < tokens.length; j++)
		{
			// for populating the document list

			if (d.containsWord(new Word(tokens[j], 0)))
			{
				d.incrementWordCount(tokens[j]);
			} else
			{
				// for recording term frequency
				// first check that the word is not already in the hash map
				if (!termDocumentOccurances.containsKey(tokens[j]))
				{
					termDocumentOccurances.put(tokens[j], 1);
				} else
				{	// word is in the hash map
					Integer currIdf = termDocumentOccurances.get(tokens[j]);
					termDocumentOccurances.remove(tokens[j]);
					termDocumentOccurances.put(tokens[j], ++currIdf);
				}

				d.addWord(new Word(tokens[j], 1));
			}
		}
		data.add(d);
	}
	
	/**
	 * // find the tfidf values and then sort the lists alphabetically
	 */
	public void cleanUp()
	{
		for (int i = 0; i < data.size(); i++)
		{
			Document currentDoc = data.get(i);
			for (String s : termDocumentOccurances.keySet())
			{
				int index;
				if ((index = currentDoc.getWordList().indexOf(new Word(s, 0))) == -1)
				{
					currentDoc.addWord(new Word(s, 0));
				} else
				{
					currentDoc.getWordList().get(index).calculateTfidf((double)data.size()/(double)termDocumentOccurances.get(s));
				}
			}
			currentDoc.sortList();
		}
	}

	//stem words
	private String stemWord(String word) 
	{
		String stemmedWord = "";
		englishStemmer es = new englishStemmer();
		es.setCurrent(word);
		es.stem();
		stemmedWord = es.getCurrent();
		return stemmedWord;
	}

	// Given an input file, reading the contents of the file and storing into an
	// HashSet
	private HashSet<String> readFileAsSetOfWords(String infile) 
	{
		try 
		{
			HashSet<String> contents = new HashSet<String>();
			BufferedReader br =
					new BufferedReader(new InputStreamReader(new FileInputStream(infile)));
			String str;
			while ((str = br.readLine()) != null) 
			{
				contents.add(str.toLowerCase());
			}
			br.close();
			return contents;
		} catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	// Returns stemmed tokens and stop-words removed
	/**
	 * Modified method to use Map instead of vector
	 * This allows us to keep a tally of term frequency together while we tokenize
	 * Also modified to additionally take in i, the docid mapping to add the length
	 * of the document to the length vector.
	 */
	private ArrayList<String> tokenizeADoc(String infile, int i) throws Exception 
	{
		HashSet<String> stopWordSet = readFileAsSetOfWords(stopWordFile);
		ArrayList<String> words = new ArrayList<String>();

		// read the file
		Scanner sc = new Scanner(infile);
		while (sc.hasNext()) 
		{
			String str = sc.nextLine();
			// tokenize the string
			StreamTokenizer tokenizer =
					new StreamTokenizer(new StringReader(str));
			tokenizer.lowerCaseMode(true);
			tokenizer.whitespaceChars(0, 64);
			tokenizer.wordChars(65, 90);
			tokenizer.whitespaceChars(91, 96);
			tokenizer.wordChars(97, 122);
			tokenizer.whitespaceChars(123, 255);
			int tt = tokenizer.nextToken();
			while(tt != StreamTokenizer.TT_EOF)
			{
				// ignoring the stop words before generating word freq map
				if (tt == StreamTokenizer.TT_WORD) 
				{ 
					// checking if tt is word
					// if word is not in stop word list & not already added in
					// wordSet, add it
					String word = tokenizer.sval;
					String stemmedWord = stemWord(tokenizer.sval);

					if (!stopWordSet.contains(word)) 
					{
						words.add(stemmedWord);
					}
					tt = tokenizer.nextToken();
				}
			}
		}
		sc.close();
		return words;
	}
}
