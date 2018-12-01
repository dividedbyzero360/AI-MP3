package MiniProject3;
import java.util.*;

public class Unigram implements IGram{

	HashMap<Character,Integer> storage = new HashMap<Character,Integer>();
	private double delta;
	private Language language;
	private int noOfCharacters=0;
	

	public Unigram(double delta, Language language) {
		this.delta = delta;
		this.language = language;
	}
	
	public Language getLanguage()
	{
		return language;
	}
	
	public void fit(List<Character> characters) 
	{
		for (int i = 0; i < characters.size() ; i++) {
			if (characters.get(i) != '+')
			{
				storage.put(characters.get(i), storage.getOrDefault(characters.get(i), 0)+1);
				noOfCharacters++;
			}
		}
		
	}
	
	public double getProbabilty(char first)
	{
		
		double numerator=storage.get(first) + delta;
		double denominator=noOfCharacters+ (delta*Resources.dictCharacters.size());
		double probability=numerator/denominator;
		return probability;
	}

	
}
