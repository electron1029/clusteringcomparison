package CS599MachineLearningP4.peaksearching;

import CS599MachineLearningP4.utils.FileParser;

public class PeakSearchingDriver 
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("Building data collection...");
		FileParser fp = new FileParser();
		fp.build();
		System.out.println("Data collection complete!");

		System.out.println();

		System.out.println("Running peak searching clustering...");
		PeakSearching p = new PeakSearching(fp.getData());
		p.cluster();
		
		for (int i = 0; i < p.getCentroids().size(); i++)
		{
			System.out.println(fp.getOriginalSentences().get(p.getCentroids().get(i).getClosestRealDocIDToCentroid()));
		}
	}
}
