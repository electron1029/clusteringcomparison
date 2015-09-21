package CS599MachineLearningP4.utils;
import java.util.ArrayList;
import java.util.Random;

public class Centroid 
{
	// variables
	private Document centroidDoc;
	private Document docMostSimilarToMean;
	private ArrayList<Document> docsInCluster;
	private double cost;
	private double maxCost;

	/**
	 * constructor
	 * initializes variables
	 * @param d
	 */
	public Centroid(Document d)
	{
		docsInCluster = new ArrayList<Document>();
		centroidDoc = d;
		docMostSimilarToMean = d;
		cost = 0;
		maxCost = -1;
	}

	/**
	 * calclates the squared error
	 * @return
	 */
	public double getSquaredError()
	{
		double error = 0;
		DocumentSimilarityCalculator c = new DocumentSimilarityCalculator();
		
		// find the euclidean distance
		for (int i = 0; i < docsInCluster.size(); i++)
		{
			error += c.euclideanDistance(centroidDoc, docsInCluster.get(i));
		}
		
		return error;
	}
	
	/**
	 * calculates the average similarity between the documents
	 * in the cluster
	 * @return
	 */
	public double getAvgSimilarity()
	{
		// we want the average distance so divide cost by num documents
		return (cost / (double)docsInCluster.size());
	}

	/**
	 * Add a doc to the cluster
	 * @param d		document
	 * @param cost	similarity
	 */
	public void addDoc(Document d, double cost)
	{
		docsInCluster.add(d);
		this.cost += cost;
		
		if(cost > maxCost)
		{
			docMostSimilarToMean = d;
			maxCost = cost;
		}
	}

	/**
	 * public access for the centroid doc
	 * @return
	 */
	public Document getCentroid()
	{
		return centroidDoc;
	}
	
	/**
	 * return the closest doc to the actual centroid
	 * @return
	 */
	public Document getClosestRealDocToCentroid()
	{
		return docMostSimilarToMean;
	}
	
	/**
	 * return just the doc id of the actual doc cloest
	 * to the centroid
	 * @return
	 */
	public int getClosestRealDocIDToCentroid() 
	{
		return docMostSimilarToMean.getDocid();
	}

	/**
	 * return the number of docs in the cluster
	 * @return
	 */
	public int getNumDocsInCluster()
	{
		return docsInCluster.size();
	}
	
	/**
	 * return the list of the documents in the cluster
	 * @return
	 */
	public ArrayList<Document> getDocsInCluster()
	{
		return docsInCluster;
	}

	/**
	 * reset the variables after a reclustering
	 */
	private void reset()
	{
		docsInCluster = new ArrayList<Document>();
		cost = 0;
		maxCost = -1;
	}

	/**
	 * Find the new centroid of the cluster based on the docs
	 * in it.
	 */
	public void findNewCentroid()
	{
		// set docid to -1 when it's not an actual document
		Document mockDocForCentroid = new Document(-1);
		for (int i = 0; i < this.centroidDoc.getWordList().size(); i++)
		{
			double temp = 1;
			// calculating arithmatic mean of cluster location
			for (Document d : this.docsInCluster)
			{
				temp += d.getWordAtIndex(i).getTfidf();
			}
			temp = temp / (double)this.docsInCluster.size();
			Word tempWord = new Word(this.centroidDoc.getWordAtIndexAsString(i), 0);
			tempWord.setTfidf(temp);
			mockDocForCentroid.addWord(tempWord);
		}
		centroidDoc = mockDocForCentroid;
		reset();
	}

	/**
	 * returns a tostring representation of the centroid
	 */
	public String toString()
	{
		String temp = "";
		for (int i = 0; i < centroidDoc.getWordList().size(); i++)
		{
			temp += " " + centroidDoc.getWordAtIndex(i);
		}
		return temp;
	}
	
	/**
	 * get a random sample of the cluster documents
	 * @param n
	 * @return
	 * @throws InterruptedException
	 */
	public ArrayList<Integer> randomSample(int n) throws InterruptedException
	{
		ArrayList<Integer> samples = new ArrayList<Integer>();
		Random r = new Random(System.currentTimeMillis());
		
		for (int i = 0; i < n; i++)
		{
			samples.add(r.nextInt(docsInCluster.size()));
			Thread.sleep(50);
		}
		
		return samples;
	}
}
