package CS599MachineLearningP4.kmeans;

import java.util.ArrayList;
import CS599MachineLearningP4.utils.Centroid;
import CS599MachineLearningP4.utils.FileParser;

public class KMeansDriver 
{
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Building data collection...");
		FileParser fp = new FileParser();
		fp.build();
		System.out.println("Data collection complete!");
		
		System.out.println();
		
		System.out.println("Starting k means...");
		KMeans k = new KMeans(fp.getData(), 10, .000001);
		k.cluster(new ArrayList<Centroid>());
		
		for (int i = 0; i < k.getCentroids().size(); i++)
		{
			System.out.println(fp.getOriginalSentences().get(k.getCentroids().get(i).getClosestRealDocIDToCentroid()));
		}
	}

}
