package MiniProject3;

import java.util.*;

public class PlayGround {

	private static ArrayList<BiGramV2> biGramList = new ArrayList<>();
	private static ArrayList<Unigram> uniGramList = new ArrayList<>();

	public static void main(String[] args) {
		double delta = 0.5;

//		System.out.println(CleanFile.getCleanString("I'd hate AI"));
//		ArrayList<Character> swedishCharacters = getAllCharacters(Arrays.asList("germanSmall.txt"));
//		System.out.println(swedishCharacters);
		ArrayList<Character> englishCharacters = getAllCharacters(Arrays.asList("en-moby-dick.txt", "en-the-little-prince.txt"));
		ArrayList<Character> frenchCharacters = getAllCharacters(Arrays.asList("fr-vingt-mille-lieues-sous-les-mers.txt", "fr-le-petit-prince.txt"));
		ArrayList<Character> swedishCharacters = getAllCharacters(Arrays.asList("german.txt"));
		//System.out.println(swedishCharacters);
		createBiGrams(delta, englishCharacters, Language.ENGLISH);
		createBiGrams(delta, frenchCharacters, Language.FRENCH);
		createBiGrams(delta, swedishCharacters, Language.GERMAN);
		createUniGrams(delta, englishCharacters, Language.ENGLISH);
		createUniGrams(delta, frenchCharacters, Language.FRENCH);
		createUniGrams(delta, swedishCharacters, Language.GERMAN);
		predict("sentence_to_predict.txt");

	}

	public static ArrayList<Character> getAllCharacters(List<String> fileNames) {
		String wholeFileString = "";
		StringBuilder sb = new StringBuilder();
		for (String fileName : fileNames) {
			sb.append(FileHandler.getString(fileName));
			sb.append(System.lineSeparator());
		}
		wholeFileString = sb.toString();
		ArrayList<Character> textCharList = CleanFile.getAllCharactersInTheString(wholeFileString);
		return textCharList;

	}

	public static  void createBiGrams(double delta, ArrayList<Character> textCharList, Language language) {

		BiGramV2 biGram = new BiGramV2(delta, language);
		biGram.fit(textCharList);
		//System.out.println(textCharList.size());
		biGramList.add(biGram);
	}

	public static void createUniGrams(double delta, ArrayList<Character> textCharList, Language language) {

		Unigram uniGram = new Unigram(delta, language);
		uniGram.fit(textCharList);
		//System.out.println(textCharList.size());
		uniGramList.add(uniGram);
	}
	
	

	public static void predict(String fileName) {
		ArrayList<String> sentences = FileHandler.getSentences(fileName);
		ArrayList<ArrayList<Character>> m = new ArrayList<>();
		for (int i = 0; i < sentences.size(); i++) {
			ArrayList<Character> textCharList = CleanFile.getAllCharactersInTheString(sentences.get(i));
			m.add(textCharList);
		}
		for (int i = 0; i < sentences.size(); i++) {
			FileHandler.incrementLineNo();
			FileHandler.writeSentences(sentences.get(i),true);
			ArrayList<Character> textCharList = m.get(i);
			print( "UNIGRAM",1,textCharList );
			FileHandler.writeSentences("--------------------------",true);
			print( "BIGRAM",2,textCharList );
			FileHandler.closeWriter();
			System.out.println("\n");
		}



	}
	
	
	
	private static void print(String model, int type,ArrayList<Character> textCharList )
	{
		HashMap<Language, Double> totalProbabilities =null;
		FileHandler.writeSentences(model+" MODEL:",true);
		if(type==1)
		{
			totalProbabilities=getUnigramResult(textCharList);
		}
		else if(type==2)
		{
			totalProbabilities=getBigramResult(textCharList);
		}
		
		Language winnerLanguage = printWinner(totalProbabilities);
		FileHandler.writeSentences("According to the "+ model +" model, the sentence is in " + winnerLanguage.toString(),true);
	}

	public static Language printWinner(HashMap<Language, Double> totalProbabilities) {
		double max = -Double.MAX_VALUE;
		Language language = Language.ENGLISH;
		for (Map.Entry<Language, Double> languageAndProbability : totalProbabilities.entrySet()) {
			if (languageAndProbability.getValue() > max) {
				max = languageAndProbability.getValue();
				language = languageAndProbability.getKey();
			}
		}
		return language;

	}

	public static HashMap<Language, Double> getBigramResult(ArrayList<Character> textCharList) {
		HashMap<Language, Double> totalProbabilities = new HashMap<Language, Double>();
		for (int j = 0; j < textCharList.size() - 1; j++) {
			if (textCharList.get(j) != '+' && textCharList.get(j + 1) != '+') {
				FileHandler.writeSentences("BIGRAM :"+textCharList.get(j)+""+textCharList.get(j + 1),false);
				for (int k = 0; k < biGramList.size(); k++) {
					BiGramV2 temp = biGramList.get(k);
					double conditionalProbability = Math.log10(temp.getConditionalProbabilty(textCharList.get(j),
							textCharList.get(j + 1)));
					updateTotalProbabilities(totalProbabilities,temp.getLanguage(),conditionalProbability);
					FileHandler.writeSentences(temp.getLanguage().toString()+ ": p("+textCharList.get(j+1)+"|"+textCharList.get(j) +") ="+conditionalProbability+"==> log prob of sentence so far: " +totalProbabilities.get(temp.getLanguage()),false);
				}
				FileHandler.writeSentences("",false);
			}
		}
		return totalProbabilities;
	}

	public static HashMap<Language, Double> getUnigramResult(ArrayList<Character> textCharList) {
		HashMap<Language, Double> totalProbabilities = new HashMap<Language, Double>();
		for (int j = 0; j < textCharList.size(); j++) {
			if (textCharList.get(j) != '+') {
				FileHandler.writeSentences("UNIGRAM :"+textCharList.get(j),false);
				for (int k = 0; k < uniGramList.size(); k++) {
					Unigram temp = uniGramList.get(k);
					double conditionalProbability = Math.log10(temp.getProbabilty(textCharList.get(j)));
					updateTotalProbabilities(totalProbabilities,temp.getLanguage(),conditionalProbability);
					FileHandler.writeSentences(temp.getLanguage().toString()+ ": p("+textCharList.get(j)+") ="+conditionalProbability+"==> log prob of sentence so far: " +totalProbabilities.get(temp.getLanguage()),false);

				}
				FileHandler.writeSentences("",false);
			}
		}
		return totalProbabilities;
	}
	
	public static void updateTotalProbabilities(HashMap<Language, Double> totalProbabilities,Language l,double conditionalProbability)
	{
		
		Double oldValue = totalProbabilities.get( l);
		totalProbabilities.put( l,oldValue != null ? oldValue + conditionalProbability : conditionalProbability);
				
	}

}






//public static void predict(String fileName) {
//	ArrayList<String> sentences = FileHandler.getSentences(fileName);
//	ArrayList<ArrayList<Character>> m = new ArrayList<>();
//	for (int i = 0; i < sentences.size(); i++) {
//		ArrayList<Character> textCharList = CleanFile.getAllCharactersInTheString(sentences.get(i));
//		m.add(textCharList);
//	}
//	for (int i = 0; i < sentences.size(); i++) {
//		System.out.println(sentences.get(i));
//		FileHandler.incrementLineNo();
//		FileHandler.writeSentences(sentences.get(i));
////		System.out.println("UNIGRAM MODEL:");
////		FileHandler.writeSentences("UNIGRAM MODEL:");
//		ArrayList<Character> textCharList = m.get(i);
////		HashMap<Language, Double> totalProbabilities = getUnigramResult(textCharList);
////		Language winnerLanguage = printWinner(totalProbabilities);
////		System.out.println("According to the unigram model, the sentence is in " + winnerLanguage.toString());
////		FileHandler.writeSentences("According to the unigram model, the sentence is in " + winnerLanguage.toString());
//		print( "UNIGRAM",1,textCharList );
//		System.out.println("--------------------------");
//		FileHandler.writeSentences("--------------------------");
//		print( "BIGRAM",2,textCharList );
////		FileHandler.writeSentences("BIGRAM MODEL:");
////		System.out.println("BIGRAM MODEL:");
////		totalProbabilities = getBigramResult(textCharList);
////		winnerLanguage = printWinner(totalProbabilities);
////		System.out.println("According to the BIGRAM model, the sentence is in " + winnerLanguage.toString());
////		FileHandler.writeSentences("According to the BIGRAM model, the sentence is in " + winnerLanguage.toString());
//		FileHandler.closeWriter();
//		System.out.println("\n");
//	}
//
//
//
//}


