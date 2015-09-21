package CS599MachineLearningP4.utils;


public class DocumentSimilarityCalculator 
{
	/**
	 * Calculate the cosine similarity between two documents
	 * @param d1
	 * @param d2
	 * @return
	 */
	public double cosineSimilarity(Document d1, Document d2)
	{
		double dotProduct = 0;
		double magnitudeD1 = 0;
		double magnitudeD2 = 0;

		// calculate
		for (int i = 0; i < d1.getDocLength(); i++)
		{
			dotProduct += d1.getWordAtIndex(i).getTfidf() * d2.getWordAtIndex(i).getTfidf();
			magnitudeD1 += Math.pow(d1.getWordAtIndex(i).getTfidf(), 2);
			magnitudeD2 += Math.pow(d2.getWordAtIndex(i).getTfidf(), 2);
		}

		magnitudeD1 = Math.sqrt(magnitudeD1);
		magnitudeD2 = Math.sqrt(magnitudeD2);

		if (magnitudeD1 == 0 || magnitudeD2 == 0)
		{
			return 0;
		} else
		{
			return dotProduct / (magnitudeD1 * magnitudeD2);
		}
	}
	
	/**
	 * Calculate the euclidean distance between two documents
	 * @param d1
	 * @param d2
	 * @return
	 */
	public double euclideanDistance(Document d1, Document d2)
	{
		double distance = 0;
		
		for (int i = 0; i < d1.getDocLength(); i++)
		{
			distance += Math.pow((d1.getWordAtIndex(i).getTfidf() - d2.getWordAtIndex(i).getTfidf()), 2); 
		}
		
		return Math.sqrt(distance);
	}
}
