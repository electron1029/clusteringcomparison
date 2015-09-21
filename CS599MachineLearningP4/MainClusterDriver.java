package CS599MachineLearningP4;

import java.util.ArrayList;
import java.util.Scanner;

import CS599MachineLearningP4.kmeans.KMeans;
import CS599MachineLearningP4.peaksearching.PeakSearching;
import CS599MachineLearningP4.utils.Centroid;
import CS599MachineLearningP4.utils.ClusterEvaluation;
import CS599MachineLearningP4.utils.FileParser;

public class MainClusterDriver 
{
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(System.in);

		System.out.println("Building data collection...");
		FileParser fp = new FileParser();
		fp.build();
		System.out.println("Data collection complete!");

		System.out.println("--------------------------------------------------");


		System.out.println("Enter 1 for kmeans or 2 for peak-searching:");
		int choice = in.nextInt();

		if (choice == 1)
		{
			System.out.println("enter k:");
			int kk = in.nextInt();
			System.out.println("Running k means clustering...");
			KMeans k = new KMeans(fp.getData(), kk, .000001);
			k.cluster(new ArrayList<Centroid>());
			System.out.println("Done!\n\n");

			ClusterEvaluation e = new ClusterEvaluation(fp.getLabels(), k.getCentroids());

			for (int i = 0; i < k.getCentroids().size(); i++)
			{
				System.out.println("Centroid " + i + ": "  +
						fp.getOriginalSentences().get(k.getCentroids().get(i).getClosestRealDocIDToCentroid()));
				System.out.println("Majority class label: " + e.getMaxLabels()[i]);
				System.out.println("Purity: " + e.getPurities().get(i));
				System.out.println("Samples:");
				ArrayList<Integer> samples = k.getCentroids().get(i).randomSample(5);
				for (int j = 0; j < samples.size(); j++)
				{
					System.out.println(fp.getOriginalSentences().get(samples.get(j)));
				}
				System.out.println("");
			}
		} else if (choice == 2)
		{

			System.out.println("Running peak searching clustering...");
			PeakSearching p = new PeakSearching(fp.getData());
			p.cluster();
			System.out.println("Done!\n\n");

			ClusterEvaluation e = new ClusterEvaluation(fp.getLabels(), p.getCentroids());

			for (int i = 0; i < p.getCentroids().size(); i++)
			{
				System.out.println("Centroid " + i + ": "  +
						fp.getOriginalSentences().get(p.getCentroids().get(i).getClosestRealDocIDToCentroid()));
				System.out.println("Majority class label: " + e.getMaxLabels()[i]);
				System.out.println("Purity: " + e.getPurities().get(i));
				System.out.println("Samples:");
				ArrayList<Integer> samples = p.getCentroids().get(i).randomSample(5);
				for (int j = 0; j < samples.size(); j++)
				{
					System.out.println(fp.getOriginalSentences().get(samples.get(j)));
				}
				System.out.println("");	
			}
		} else
		{
			System.out.println("An error occurred with console input.");
			System.exit(1);
		}
	}
}
