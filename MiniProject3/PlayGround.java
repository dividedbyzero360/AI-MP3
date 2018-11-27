package MiniProject3;

import java.util.*;

public class PlayGround {

	private static ArrayList<BiGramV2> biGramList = new ArrayList<>();
	private static HashMap<Language, Double> totalProbabilities=new HashMap<Language, Double>();
	public static void main(String[] args) {
		double delta = 0.5;
		createBiGrams(delta,"en-moby-dick.txt", Language.ENGLISH);
		createBiGrams(delta,"fr-vingt-mille-lieues-sous-les-mers.txt", Language.FRENCH);
		
		
//		for(int i=0; i<biGramList.size();i++)
//		{
//			totalProbabilities.put(biGramList.get(i).getLanguage(), new Double(0));
//		}
		predict("sentence_to_predict.txt");
	}

	public static void createBiGrams(double delta, String fileName, Language language) {
		String wholeFileString = FileHandler.getString(fileName);
		ArrayList<Character> textCharList = CleanFile.getAllCharactersInTheString(wholeFileString);
		BiGramV2 biGram = new BiGramV2(delta, language);
		biGram.fit(textCharList);
		System.out.println(textCharList.size());
		biGramList.add(biGram);
	}

	private static void predict(String fileName) {
		ArrayList<String> sentences=FileHandler.getSentences(fileName);
		for(int i=0;i< sentences.size();i++)
		{
			System.out.println(sentences.get(i));
			ArrayList<Character> textCharList = CleanFile.getAllCharactersInTheString(sentences.get(i));
			System.out.println(textCharList);
			for(int j=0;j<textCharList.size()-1;j++)
			{
				if(textCharList.get(j)!='+' && textCharList.get(j+1)!='+')
				{
					for(int k=0;k<biGramList.size();k++)
					{
						BiGramV2 temp=biGramList.get(k);
						double conditionalProbability=temp.getConditionalProbabilty(textCharList.get(j), textCharList.get(j+1));
						conditionalProbability=Math.log10(conditionalProbability);
//						System.out.println(temp.getLanguage());
//						System.out.println(totalProbabilities.get(temp.getLanguage()));
						Double oldValue=totalProbabilities.get(temp.getLanguage());
						totalProbabilities.put(temp.getLanguage(), oldValue !=null ?oldValue+conditionalProbability: conditionalProbability);
					}
					
				}
			}
			
			double max=-1;
			Language language=Language.ENGLISH;
			for(Map.Entry<Language,Double> languageAndProbability: totalProbabilities.entrySet())
			{
				if(languageAndProbability.getValue() > max)
				{
					max=languageAndProbability.getValue();
					language=languageAndProbability.getKey();
				}
			}
			System.out.println("According to the bigram model, the sentence is in "+language.toString());
			
			totalProbabilities.clear();
			
			
		}
      
	}

}
