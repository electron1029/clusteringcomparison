package CS599MachineLearningP4.kmeans;

import java.util.ArrayList;
import java.util.Random;

import CS599MachineLearningP4.utils.Centroid;
import CS599MachineLearningP4.utils.Document;
import CS599MachineLearningP4.utils.DocumentSimilarityCalculator;

public class KMeans 
{
	//variables
	private ArrayList<Document> data;
	private ArrayList<Centroid> centroids;
	private int numClusters;
	private double errorStep;

	/**
	 * Constructor.
	 * @param data					parsed file data
	 * @param numClusters			how many clusters
	 * @param maxIterations			how many iterations
	 */
	public KMeans(ArrayList<Document> data, int numClusters, double errorStep)
	{
		this.data = data;
		centroids = new ArrayList<Centroid>();
		this.numClusters = numClusters;
		this.errorStep = errorStep;
	}

	/**
	 * Returns the list of centroids.
	 * @return
	 */
	public ArrayList<Centroid> getCentroids()
	{
		return centroids;
	}

	/**
	 * Main clustering algorithm for k-means.
	 * @param centroidList
	 */
	public void cluster(ArrayList<Centroid> centroidList)
	{
		DocumentSimilarityCalculator c = new DocumentSimilarityCalculator();
		double deltaError = Double.MAX_VALUE;
		double LSSLastIteration = 0;
		double LSSThisIteration = 0;
		boolean firstIteration = true;

		// choose k random centroids
		if (centroidList.isEmpty())
		{
			selectCentroids();		// selects the random centroids if they were not in the list
		} else
		{
			this.centroids = centroidList;
		}

		while (deltaError > errorStep)	// stopping condition
		{
			if (!firstIteration)
			{
				// recalculate means
				for (int j = 0; j < centroids.size(); j++)
				{
					centroids.get(j).findNewCentroid();
				}
			}
			firstIteration = false;

			LSSLastIteration = LSSThisIteration;
			LSSThisIteration = 0;

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

			// find totalError
			for (int i = 0; i < centroids.size(); i++)
			{
				LSSThisIteration += centroids.get(i).getSquaredError();
			}
			// find deltaError (between last and this)
			deltaError = Math.abs(LSSThisIteration - LSSLastIteration);
		}
	}

	/**
	 * Function to select random centroids.
	 */
	private void selectCentroids()
	{
		Random r = new Random(System.currentTimeMillis());
		centroids = new ArrayList<Centroid>();

		for (int i = 0; i < numClusters; i++)
		{
			centroids.add(new Centroid(data.get(r.nextInt((int)((double)((i + 1) * data.size())/((double)numClusters))))));
		}
	}
}
