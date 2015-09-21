package CS599MachineLearningP4.peaksearching;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import CS599MachineLearningP4.utils.Centroid;
import CS599MachineLearningP4.utils.Document;
import CS599MachineLearningP4.utils.DocumentSimilarityCalculator;
import CS599MachineLearningP4.utils.MaxHeap;
import CS599MachineLearningP4.utils.Tuple;

public class PeakSearching 
{
	//varaibles
	private double[][] adjacencyMatrix;
	private double[][] cosineSimMatrix;
	private double[] d;
	private double[] h;
	private int [] persistency;
	private ArrayList<Document> data;
	private ArrayList<Integer> peakPoints;
	private ArrayList<Centroid> centroids;

	/**
	 * Constructor
	 * Initializes all required data structures
	 * @param data  	the parsed document data
	 */
	public PeakSearching(ArrayList<Document> data)
	{
		this.data = data;
		adjacencyMatrix = new double[data.size()][data.size()];
		cosineSimMatrix = new double[data.size()][data.size()];
		d = new double[data.size()];
		h = new double[data.size()];
		persistency = new int[data.size()];
	}
	
	/** 
	 * Accessor method to return the calculated centroids
	 * @return
	 */
	public ArrayList<Centroid> getCentroids()
	{
		return centroids;
	}

	/**
	 * Find the coocurrance matrix for all documents.
	 * Coocurance used is the number of terms common between
	 * two documents.
	 */
	private void calculateAdjacencyMatrix()
	{
		for (int i = 0; i < adjacencyMatrix.length; i++)
		{
			for (int j = 0; j < adjacencyMatrix.length; j++)
			{
				if (i == j)
				{
					adjacencyMatrix[i][j] = 0;
					continue;
				}
				Document d1 = data.get(i);
				Document d2 = data.get(j);

				for (int k = 0; k < d1.getWordList().size(); k++)
				{
					int freq1 = d1.getWordAtIndex(k).getFrequency();
					int freq2 = d2.getWordAtIndex(k).getFrequency();
					if (freq1 > 0 && freq2 > 0)
					{
						adjacencyMatrix[i][j] = Math.abs(freq1 - freq2);
					}
				}
			}
		}
	}
	
	/**
	 * Calculates the cosine similarity matrix.
	 * This does it once and stores it to save time.
	 */
	private void calculateCosineSimMatrix()
	{
		DocumentSimilarityCalculator c = new DocumentSimilarityCalculator();
		for (int i = 0; i < cosineSimMatrix.length; i++)
		{
			for (int j = 0; j < cosineSimMatrix.length; j++)
			{
				if (i == j)
				{
					cosineSimMatrix[i][j] = 0;
				} else
				{
					cosineSimMatrix[i][j] = c.cosineSimilarity(data.get(i), data.get(j));
				}
			} 
		}
	}

	/**
	 * Calculates array D needed for peak clustering
	 * This is defined as:
	 * 	di = sum(j=1 -> m) Wi,j
	 */
	private void calculateD()
	{
		for (int i = 0; i < d.length; i++)
		{
			for (int j = 0; j < d.length; j++)
			{
				d[i] += adjacencyMatrix[i][j];
			}
		}
	}

	/**
	 * Calculates array H needed for peak clustering
	 * This is defined as:
	 * 	hi = [sum(j=1 -> m)[sum(j=1 -> m)Wi,j*dj]/di]
	 */
	private void calculateH()
	{
		double innerSum;

		for (int i = 0; i < data.size(); i++)  
		{
			innerSum = 0;
			
			for (int j = 0; j < data.size(); j++) // inner sum, j = 1 to m
			{
				innerSum += adjacencyMatrix[i][j] * d[j];
			}
			
			h[i] = innerSum / d[i];
		}
	}

	/**
	 * Main peak searching clustering algorithm
	 */
	public void cluster()
	{
		// helper variables
		peakPoints = new ArrayList<Integer>();
		double[] similarityArray = new double[d.length];
		int c = -1;
		int currMax = -1;

		// first we find the coorurrance matrix
		calculateAdjacencyMatrix();
		
		// calculate the similarity matrix
		calculateCosineSimMatrix();

		// calculate the D array
		calculateD();

		// calculate the h array
		calculateH();

		// find the initial peak point
		int seed = findMax(d);
		peakPoints.add(seed);

		// find all peak points
		while(true)
		{
			// find persistencies for xis
			System.arraycopy(d, 0, similarityArray, 0, d.length);

			// set all persistencies to 0
			for (int i = 0; i < persistency.length; i++)
			{
				persistency[i] = 0;
			}

			// find k nearest neighbors of all peak points
			for (int k = 0; k < data.size(); k++)	// for k = 1...m
			{
				// remove all xi in k nearest neighbors from consideration
				for (int j = 0; j < peakPoints.size(); j++)
				{
					similarityArray[peakPoints.get(j)] = -1;

					Vector<Tuple<Document, Double>> kNearestNeighbors = findKNearestNeighbors(k, peakPoints.get(j));
					for (int i = 0; i < kNearestNeighbors.size(); i++)
					{
						similarityArray[kNearestNeighbors.get(i).getK().getDocid()] = -1;
					}
				}

				currMax = findMax(similarityArray);
				// all points are out of the running somehow so much quit trying
				if (currMax == -1)
				{
					break;
				}
				persistency[currMax] += 1;
			}

			// find highest persistency
			c = findMax(persistency);

			// find the point with the max persistency after the above calculations
			// if d[c] > h[c] for this point, add it to the peak points and continue
			// finding peak points.  otherwise stop and compile results.
			if (d[c] > h[c])
			{
				peakPoints.add(c);
			} else
			{
				organizeClusters();
				return;
			}
		}
	}

	/**
	 * Find the index of the max value in a 1D array of doubles
	 * @param doubleArray		the array to find the max in
	 * @return					index of the max element
	 */
	private int findMax(double[] doubleArray)
	{
		int maxDIndex = -1;
		double maxD = 0;

		for (int i = 0; i < doubleArray.length; i++)
		{
			if (doubleArray[i] > maxD)
			{
				maxD = doubleArray[i];
				maxDIndex = i;
			}
		}

		return maxDIndex;
	}

	/**
	 * Find the index of the max value in a 1D array of ints
	 * @param intArray		the array to find the max in
	 * @return				index of the max element
	 */
	private int findMax(int[] intArray)
	{
		int maxDIndex = -1;
		double maxD = 0;

		for (int i = 0; i < intArray.length; i++)
		{
			if (intArray[i] > maxD)
			{
				maxD = intArray[i];
				maxDIndex = i;
			}
		}

		return maxDIndex;
	}

	/**
	 * Finds the k most similar documents to the document at index kindex
	 * @param k				number of neighbors to find
	 * @param xindex		index of the centroid
	 * @return				list of the k nearest neighbors
	 */
	private Vector<Tuple<Document, Double>> findKNearestNeighbors(int k, int xindex)
	{
		MaxHeap<Tuple<Document, Double>> kNearestNeighbors = new MaxHeap<Tuple<Document, Double>>();
		double similarity = -1;

		for (int i = 0; i < data.size(); i++)
		{
			similarity = cosineSimMatrix[xindex][i];
			kNearestNeighbors.insert(new Tuple<Document, Double>(data.get(i), similarity));
		}
		return kNearestNeighbors.readTopK(k);
	}

	/**
	 * Cluster the documents based on peak points
	 */
	private void organizeClusters()
	{
		centroids = new ArrayList<Centroid>();
		DocumentSimilarityCalculator c = new DocumentSimilarityCalculator();
		
		// initialize all the centroids to the peak points
		for (int i = 0; i < peakPoints.size(); i++)
		{
			centroids.add(new Centroid(data.get(peakPoints.get(i))));
		}
		
		// assign each document to the closest cluster
		for (int j = 0; j < data.size(); j++)	// for each document
		{
			double currentMax = -1;
			int mostSimilarClusterIndex = Integer.MAX_VALUE;
			// calculate most similar centroid for all docs
			for (int k = 0; k < centroids.size(); k++)	// find the most similar centroid
			{
				double result = c.cosineSimilarity(centroids.get(k).getCentroid(), data.get(j));
				if (result > currentMax)
				{
					currentMax = result;
					mostSimilarClusterIndex = k;
				} 
			}
			centroids.get(mostSimilarClusterIndex).addDoc(data.get(j), currentMax); // add the doc to the centroid's list
		}
	}
}