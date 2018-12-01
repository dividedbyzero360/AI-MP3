package MiniProject3;
import java.util.List;

public class BiGramV2  implements IGram{
	private double delta;
	private Language language;

	public BiGramV2(double delta, Language language) {
		this.delta = delta;
		this.language = language;
	}


	double[][] storage = new double[Resources.dictCharacters.size()][Resources.dictCharacters.size()];
	private double[] countOfRows = new double[Resources.dictCharacters.size()];

	public void fit(List<Character> characters) {
		for (int i = 0; i < characters.size() - 1; i++) {
		if (characters.get(i) != '+' && characters.get(i + 1) != '+')
		{
				int rowNo = Resources.dictCharacters.indexOf(characters.get(i));
				int columnNo = Resources.dictCharacters.indexOf(characters.get(i + 1));
				storage[rowNo][columnNo]++;
				countOfRows[rowNo]++;
			}

		}

	}
	
	public Language getLanguage()
	{
		return language;
	}

	public double getConditionalProbabilty(char first, char second)
	{
		//System.out.println(first + " "+second);
		int rowNo = Resources.dictCharacters.indexOf(first);
		int columnNo = Resources.dictCharacters.indexOf(second);
		double numerator=storage[rowNo][columnNo] + delta;
		//System.out.println(numerator);
		double denominator=countOfRows[rowNo]+ (delta*Resources.dictCharacters.size());
//		System.out.println("countOfRows "+ countOfRows[rowNo]);
//		System.out.println(denominator);
		double conditionalProbability=numerator/denominator;
//		System.out.println(conditionalProbability);
		return conditionalProbability;
	}
	
}
