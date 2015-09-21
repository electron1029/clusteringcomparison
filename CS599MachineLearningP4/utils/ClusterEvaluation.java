package CS599MachineLearningP4.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Cluster evaluation.
 * Only has method for purity
 * @author cfry
 *
 */
public class ClusterEvaluation 
{
	// variables
	private ArrayList<Centroid> clusters;
	private HashMap<Integer,String> labels;
	private ArrayList<Double> purities;
	private String maxLabels[];
	private int maxCounts[];

	/**
	 * Constructor
	 * @param labels	list of all labels to docids
	 * @param clusters	list of all clusters
	 */
	public ClusterEvaluation(HashMap<Integer,String> labels, ArrayList<Centroid> clusters)
	{
		purities = new ArrayList<Double>();
		this.labels = labels;
		this.clusters = clusters;
		calculatePurity();
	}
	
	/** 
	 * public accessor for purities variable
	 * @return
	 */
	public ArrayList<Double> getPurities()
	{
		return purities;
	}
	
	/**
	 * public accessor for max labels variable
	 * @return
	 */
	public String[] getMaxLabels()
	{
		return maxLabels;
	}
	
	/**
	 * public accessor for max counts
	 * @return
	 */
	public int[] maxCounts()
	{
		return maxCounts;
	}

	/**
	 * Calculates cluster purity
	 */
	private void calculatePurity()
	{
		ArrayList<ArrayList<Tuple<String, Integer>>> clusterCounts = new ArrayList<ArrayList<Tuple<String, Integer>>>();
		
		// first count the number of times each label occurs in a cluster
		// remember the counts and add them to a list for later
		for (int i = 0; i < clusters.size(); i++)
		{
			ArrayList<Document> docs = clusters.get(i).getDocsInCluster();
			ArrayList<Tuple<String,Integer>> currCounts = new ArrayList<Tuple<String,Integer>>();
			for (int j = 0; j < clusters.get(i).getNumDocsInCluster(); j++)
			{
				int index;
				if ((index = currCounts.indexOf(new Tuple<String,Integer>(labels.get(docs.get(j).getDocid()), 0))) != -1)
				{
					currCounts.get(index).setV(currCounts.get(index).getV() + 1);
				} else
				{
					currCounts.add(new Tuple<String,Integer> (labels.get(docs.get(j).getDocid()), 1));
				}
			}
			clusterCounts.add(currCounts);
		}

		// now we find the max label in each cluster
		maxLabels = new String[clusterCounts.size()];
		maxCounts = new int[clusterCounts.size()];
		for (int i = 0; i < clusterCounts.size(); i++)
		{
			for (int j = 0; j < clusterCounts.get(i).size(); j++)
			{
				if (clusterCounts.get(i).get(j).getV() > maxCounts[i])
				{
					maxLabels[i] = clusterCounts.get(i).get(j).getK();
					maxCounts[i] = clusterCounts.get(i).get(j).getV();
				}
			}
		}
			
		// now we finally calculate the purities
		for (int i = 0; i < maxLabels.length; i++)
		{
			purities.add((double)maxCounts[i] / (double)clusters.get(i).getNumDocsInCluster());
		}
	}
}
