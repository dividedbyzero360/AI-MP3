package MiniProject3;

import java.util.Arrays;
import java.util.List;

public class BiGramV2 {
	private double delta;
	private Language language;

	public BiGramV2(double delta, Language language) {
		this.delta = delta;
		this.language = language;
	}

	public static List<Character> dictCharacters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
	double[][] storage = new double[dictCharacters.size()][dictCharacters.size()];
	private double[] countOfRows = new double[dictCharacters.size()];

	public void fit(List<Character> characters) {
		for (int i = 0; i < characters.size() - 1; i++) {
		if (characters.get(i) != '+' && characters.get(i + 1) != '+')
		{
				int rowNo = dictCharacters.indexOf(characters.get(i));
				int columnNo = dictCharacters.indexOf(characters.get(i + 1));
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
		int rowNo = dictCharacters.indexOf(first);
		int columnNo = dictCharacters.indexOf(second);
		double numerator=storage[rowNo][columnNo] + delta;
		double denominator=countOfRows[rowNo]+ (delta*dictCharacters.size());
		double conditionalProbability=numerator/denominator;
		return conditionalProbability;
	}
}
