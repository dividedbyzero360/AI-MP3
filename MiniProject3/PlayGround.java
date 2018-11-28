package MiniProject3;

import java.util.*;

public class PlayGround {

	private static ArrayList<BiGramV2> biGramList = new ArrayList<>();
	private static ArrayList<Unigram> uniGramList = new ArrayList<>();

	public static void main(String[] args) {
		double delta = 0.5;

		ArrayList<Character> englishCharacters = getAllCharacters(
				Arrays.asList("en-moby-dick.txt", "en-the-little-prince.txt"));
		ArrayList<Character> frenchCharacters = getAllCharacters(
				Arrays.asList("fr-vingt-mille-lieues-sous-les-mers.txt", "fr-le-petit-prince.txt"));
		createBiGrams(delta, englishCharacters, Language.ENGLISH);
		createBiGrams(delta, frenchCharacters, Language.FRENCH);
		createUniGrams(delta, englishCharacters, Language.ENGLISH);
		createUniGrams(delta, frenchCharacters, Language.FRENCH);

		// for(int i=0; i<biGramList.size();i++)
		// {
		// totalProbabilities.put(biGramList.get(i).getLanguage(), new
		// Double(0));
		// }
		fun("sentence_to_predict.txt");

		// System.out.println(CleanFile.getCleanString("warabir'adf"));

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

	public static void createBiGrams(double delta, ArrayList<Character> textCharList, Language language) {

		BiGramV2 biGram = new BiGramV2(delta, language);
		biGram.fit(textCharList);
		System.out.println(textCharList.size());
		biGramList.add(biGram);
	}

	public static void createUniGrams(double delta, ArrayList<Character> textCharList, Language language) {

		Unigram uniGram = new Unigram(delta, language);
		uniGram.fit(textCharList);
		System.out.println(textCharList.size());
		uniGramList.add(uniGram);
	}

	public static void fun(String fileName) {
		ArrayList<String> sentences = FileHandler.getSentences(fileName);
		ArrayList<ArrayList<Character>> m = new ArrayList<>();
		for (int i = 0; i < sentences.size(); i++) {
			ArrayList<Character> textCharList = CleanFile.getAllCharactersInTheString(sentences.get(i));
			m.add(textCharList);
		}
		for (int i = 0; i < sentences.size(); i++) {
			System.out.println(sentences.get(i));
			System.out.println("UNIGRAM MODEL:");
			ArrayList<Character> textCharList = m.get(i);
			HashMap<Language, Double> totalProbabilities = getUnigramResult(textCharList);
			Language winnerLanguage = printWinner(totalProbabilities);
			System.out.println("According to the unigram model, the sentence is in " + winnerLanguage.toString());
			System.out.println("\n");
		}

		System.out.println("\n");
		for (int i = 0; i < sentences.size(); i++) {
			System.out.println(sentences.get(i));
			System.out.println("BIGRAM MODEL:");
			ArrayList<Character> textCharList = m.get(i);
			HashMap<Language, Double> totalProbabilities = getBigramResult(textCharList);
			Language winnerLanguage = printWinner(totalProbabilities);
			System.out.println("According to the unigram model, the sentence is in " + winnerLanguage.toString());
			System.out.println("\n");
		}

	}

	public static Language printWinner(HashMap<Language, Double> totalProbabilities) {
		double max = -Double.MAX_VALUE;
		Language language = Language.ENGLISH;
		for (Map.Entry<Language, Double> languageAndProbability : totalProbabilities.entrySet()) {
			//System.out.println(languageAndProbability.getValue() + " " + languageAndProbability.getKey());
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
				for (int k = 0; k < biGramList.size(); k++) {
					BiGramV2 temp = biGramList.get(k);
					double conditionalProbability = temp.getConditionalProbabilty(textCharList.get(j),
							textCharList.get(j + 1));
					conditionalProbability = Math.log10(conditionalProbability);
					Double oldValue = totalProbabilities.get(temp.getLanguage());
					totalProbabilities.put(temp.getLanguage(),
							oldValue != null ? oldValue + conditionalProbability : conditionalProbability);

				}

			}
		}
		return totalProbabilities;
	}

	public static HashMap<Language, Double> getUnigramResult(ArrayList<Character> textCharList) {
		HashMap<Language, Double> totalProbabilities = new HashMap<Language, Double>();
		for (int j = 0; j < textCharList.size(); j++) {
			if (textCharList.get(j) != '+') {
				for (int k = 0; k < uniGramList.size(); k++) {
					Unigram temp = uniGramList.get(k);
					double conditionalProbability = temp.getProbabilty(textCharList.get(j));
					conditionalProbability = Math.log10(conditionalProbability);
					Double oldValue = totalProbabilities.get(temp.getLanguage());
					totalProbabilities.put(temp.getLanguage(),
							oldValue != null ? oldValue + conditionalProbability : conditionalProbability);

				}

			}
		}
		return totalProbabilities;
	}

}

// private static void predict(String fileName) {
// ArrayList<String> sentences=FileHandler.getSentences(fileName);
// ArrayList<ArrayList<Character>> m=new ArrayList<>();
// for(int i=0;i< sentences.size();i++)
// {
// ArrayList<Character> textCharList =
// CleanFile.getAllCharactersInTheString(sentences.get(i));
// m.add(textCharList);
// }
//
// for (int i = 0; i < sentences.size(); i++) {
// System.out.println(sentences.get(i));
// System.out.println("UNIGRAM MODEL:");
// ArrayList<Character> textCharList = m.get(i);
// for (int j = 0; j < textCharList.size() ; j++) {
// if (textCharList.get(j) != '+') {
// for (int k = 0; k < uniGramList.size(); k++) {
// Unigram temp = uniGramList.get(k);
// double conditionalProbability = temp.getProbabilty(textCharList.get(j));
// conditionalProbability = Math.log10(conditionalProbability);
// Double oldValue = totalProbabilities.get(temp.getLanguage());
// totalProbabilities.put(temp.getLanguage(),
// oldValue != null ? oldValue + conditionalProbability :
// conditionalProbability);
//
// }
//
// }
// }
// double max=-Double.MAX_VALUE;
// Language language=Language.ENGLISH;
// for(Map.Entry<Language,Double> languageAndProbability:
// totalProbabilities.entrySet())
// {
// System.out.println(languageAndProbability.getValue() + "
// "+languageAndProbability.getKey() );
// if(languageAndProbability.getValue() > max)
// {
// max=languageAndProbability.getValue();
// language=languageAndProbability.getKey();
// }
// }
// System.out.println("According to the Unigram model, the sentence is in
// "+language.toString());
//
// totalProbabilities.clear();
//
//
// }
//
// for(int i=0;i< sentences.size();i++)
// {
// System.out.println(sentences.get(i));
// System.out.println("BiGram MODEL:");
// ArrayList<Character> textCharList = m.get(i);
// for(int j=0;j<textCharList.size()-1;j++)
// {
// if(textCharList.get(j)!='+' && textCharList.get(j+1)!='+')
// {
// for(int k=0;k<biGramList.size();k++)
// {
// BiGramV2 temp=biGramList.get(k);
// double
// conditionalProbability=temp.getConditionalProbabilty(textCharList.get(j),
// textCharList.get(j+1));
// conditionalProbability=Math.log10(conditionalProbability);
// Double oldValue=totalProbabilities.get(temp.getLanguage());
// totalProbabilities.put(temp.getLanguage(), oldValue !=null
// ?oldValue+conditionalProbability: conditionalProbability);
//
// }
//
// }
// }
//
//
//
// double max=-Double.MAX_VALUE;
// Language language=Language.ENGLISH;
// for(Map.Entry<Language,Double> languageAndProbability:
// totalProbabilities.entrySet())
// {
// System.out.println(languageAndProbability.getValue() + "
// "+languageAndProbability.getKey() );
// if(languageAndProbability.getValue() > max)
// {
// max=languageAndProbability.getValue();
// language=languageAndProbability.getKey();
// }
// }
// System.out.println("According to the bigram model, the sentence is in
// "+language.toString());
//
// totalProbabilities.clear();
// }
//
// }

// private static void predict(String fileName) {
// ArrayList<String> sentences=FileHandler.getSentences(fileName);
// for(int i=0;i< sentences.size();i++)
// {
// System.out.println(sentences.get(i));
// ArrayList<Character> textCharList =
// CleanFile.getAllCharactersInTheString(sentences.get(i));
// System.out.println(textCharList);
// for(int j=0;j<textCharList.size()-1;j++)
// {
// if(textCharList.get(j)!='+' && textCharList.get(j+1)!='+')
// {
// for(int k=0;k<biGramList.size();k++)
// {
// BiGramV2 temp=biGramList.get(k);
// double
// conditionalProbability=temp.getConditionalProbabilty(textCharList.get(j),
// textCharList.get(j+1));
// conditionalProbability=Math.log10(conditionalProbability);
//// System.out.println(temp.getLanguage());
//// System.out.println(totalProbabilities.get(temp.getLanguage()));
// Double oldValue=totalProbabilities.get(temp.getLanguage());
// totalProbabilities.put(temp.getLanguage(), oldValue !=null
// ?oldValue+conditionalProbability: conditionalProbability);
// //[j, a, i, m, e, +, l, i, a]
// //[i, +, h, a, t, e, +, a, i, +
// //char[] arra=new char[]{'j', 'a', 'i', 'm', 'e', '+', 'l', 'i', 'a'};
//// char[] arra=new char[]{'i', '+', 'h', 'a', 't', 'e', '+', 'a', 'i', '+'};
//// for(int d=0;d<arra.length-1;d++)
//// {
////// if(arra[d]!='+' && arra[d+1]!='+')
////// {
//// int row=BiGramV2.dictCharacters.indexOf(arra[d]);
//// int column=BiGramV2.dictCharacters.indexOf(arra[d+1]);
//// System.out.println(arra[d]+" "+arra[d+1] + "
// "+(temp.storage[row][column]));
////// }
//// }
//// System.out.println("----------------------------------------------------------");
//
//
// }
//
// }
// }
//
//
//
// double max=-Double.MAX_VALUE;
// Language language=Language.ENGLISH;
// for(Map.Entry<Language,Double> languageAndProbability:
// totalProbabilities.entrySet())
// {
// System.out.println(languageAndProbability.getValue() + "
// "+languageAndProbability.getKey() );
// if(languageAndProbability.getValue() > max)
// {
// max=languageAndProbability.getValue();
// language=languageAndProbability.getKey();
// }
// }
// System.out.println("According to the bigram model, the sentence is in
// "+language.toString());
//
// totalProbabilities.clear();
// }
//
// }
