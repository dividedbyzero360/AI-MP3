package MiniProject3;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class PlayGround {

	private static ArrayList<BiGramV2> biGramList = new ArrayList<>();
	private static ArrayList<Unigram> uniGramList = new ArrayList<>();
	
	static HashMap<GramType,ArrayList<IGram>> gramsCollection=new HashMap<GramType,ArrayList<IGram>>();
	
	

	public static void main(String[] args) {
		gramsCollection.put(GramType.UNIGRAM, new ArrayList<IGram>());
		gramsCollection.put(GramType.BIGRAM, new ArrayList<IGram>());
		double delta = 0.5;
		ArrayList<Character> englishCharacters = getAllCharacters(Arrays.asList("en-moby-dick.txt", "en-the-little-prince.txt"));
		ArrayList<Character> frenchCharacters = getAllCharacters(Arrays.asList("fr-vingt-mille-lieues-sous-les-mers.txt", "fr-le-petit-prince.txt"));
		ArrayList<Character> germanCharacters = getAllCharacters(Arrays.asList("Kritik-der-reinen-Vernunft.txt","Buddenbrooks.txt"));
		createGrams(delta, englishCharacters, Language.ENGLISH);
		createGrams(delta, frenchCharacters, Language.FRENCH);
		createGrams(delta, germanCharacters, Language.GERMAN);
		
		CompletableFuture.runAsync(()->{
			writeDumps();
		});
		predict("sentence_to_predict.txt");

	}
	
	public static void writeDumps()
	{
		for(Map.Entry<GramType, ArrayList<IGram>> keyValueGrams : gramsCollection.entrySet())
		{
			ArrayList<IGram> grams=keyValueGrams.getValue();
			for(int i=0;i < grams.size();i++)
			{
				ArrayList<String> dump=new ArrayList<String>();
				Language l=null;
				if(keyValueGrams.getKey()==GramType.UNIGRAM)
				{
					Unigram temp=(Unigram)grams.get(i);
					l=temp.getLanguage();
					for(char c: Resources.dictCharacters)
					{
						dump.add("("+ c+") = "+ temp.getProbabilty(c));
					}
					
					
				}
				
				else if(keyValueGrams.getKey()==GramType.BIGRAM)
				{
					BiGramV2 temp=(BiGramV2)grams.get(i);
					l=temp.getLanguage();
					for(char firstCharacter: Resources.dictCharacters)
					{
						for(char secondCharacter: Resources.dictCharacters)
						{
							dump.add("("+secondCharacter+"|"+firstCharacter+") = "+ temp.getConditionalProbabilty(firstCharacter, secondCharacter));
						}
						
					}
				}
				FileHandler.writeDump(l, keyValueGrams.getKey(), dump);
			}
			
		}
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
	
	
	public static  void createGrams(double delta, ArrayList<Character> textCharList, Language language) 
	{
		BiGramV2 biGram = new BiGramV2(delta, language);
		biGram.fit(textCharList);
		biGramList.add(biGram);
		gramsCollection.get(GramType.BIGRAM).add(biGram);
		Unigram uniGram = new Unigram(delta, language);
		uniGram.fit(textCharList);
		uniGramList.add(uniGram);
		gramsCollection.get(GramType.UNIGRAM).add(uniGram);
		System.out.println("Language "+ language.toString() +" "+ textCharList.size());
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
			print(GramType.UNIGRAM,textCharList );
			FileHandler.writeSentences("--------------------------",true);
			print( GramType.BIGRAM,textCharList );
			FileHandler.closeWriter();
			System.out.println("\n");
		}
	}
	
	
	
	private static void print(GramType type,ArrayList<Character> textCharList )
	{
		HashMap<Language, Double> totalProbabilities =null;
		FileHandler.writeSentences(type.toString()+" MODEL:",true);
//		totalProbabilities=getResult(textCharList, type);
//		Language winnerLanguage = printWinner(totalProbabilities);
//		FileHandler.writeSentences("According to the "+ type.toString() +" model, the sentence is in " + winnerLanguage.toString(),true);
		totalProbabilities=getResult2(textCharList, type);
		Language winnerLanguage = printWinner(totalProbabilities);
		FileHandler.writeSentences("According to the "+ type.toString() +" model, the sentence is in " + winnerLanguage.toString(),true);
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
	
	
	
	public static HashMap<Language, Double> getResult(ArrayList<Character> textCharList, GramType type)
	{
		HashMap<Language, Double> totalProbabilities = new HashMap<Language, Double>(); 
		if(type==GramType.BIGRAM)
		{
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
		}
		else if(type==GramType.UNIGRAM)
		{
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
		}
		
		
		return totalProbabilities;
	}
	
	
	
	public static HashMap<Language, Double> getResult2(ArrayList<Character> textCharList, GramType type)
	{
		HashMap<Language, Double> totalProbabilities = new HashMap<Language, Double>();
		int size=0;
		BiPredicate<ArrayList<Character>, Integer> condition=null;
	    BiFunction<ArrayList<Character>, Integer , String> firstStatetementToPrint=null;
	    BiFunction<ArrayList<Character>, Integer , Function<Double,String>> secondStatetementToPrint=null;
	    ArrayList<IGram> gramsList=gramsCollection.get(type);
		if(type==GramType.BIGRAM)
		{
			 size=textCharList.size() - 1;
			 condition= ( list,  i)->{ return list.get(i)!='+' && list.get(i+1)!='+';}; 
			 firstStatetementToPrint=(list,  i)-> { return "BIGRAM :"+list.get(i)+""+list.get(i + 1);};
			 secondStatetementToPrint=(list,  j)-> { 
				 return (conditionalProbability)-> {
					 return ": p("+list.get(j+1)+"|"+list.get(j) +") ="+conditionalProbability+"==> log prob of sentence so far: ";
					 }; 
				};
			 
		}
		else if(type==GramType.UNIGRAM){
			size=textCharList.size() ;
			condition= ( list,  i)->{ return list.get(i)!='+';};
			firstStatetementToPrint=(list,  i)-> { return  "UNIGRAM :"+list.get(i);};
			secondStatetementToPrint=(list,  j)-> { 
				 return (conditionalProbability)-> {
					 return ": p("+list.get(j)+") ="+conditionalProbability+"==> log prob of sentence so far: ";
					 }; 
				};
		}
		
		for (int j = 0; j < size; j++) 
		{
			if(condition.test(textCharList, j))
			{
				FileHandler.writeSentences(firstStatetementToPrint.apply(textCharList, j),false);
				for (int k = 0; k < gramsList.size(); k++) 
				{
					double conditionalProbability=0;
					IGram temp=gramsList.get(k);
					if(temp instanceof Unigram )
					{
						conditionalProbability =Math.log10(((Unigram)temp).getProbabilty(textCharList.get(j)));
					}
					else if(temp instanceof BiGramV2)
					{
						conditionalProbability =Math.log10(((BiGramV2)temp).getConditionalProbabilty(textCharList.get(j),textCharList.get(j + 1)));
								
					}
					updateTotalProbabilities(totalProbabilities,temp.getLanguage(),conditionalProbability);
					FileHandler.writeSentences(temp.getLanguage().toString()+ secondStatetementToPrint.apply(textCharList, j).apply(conditionalProbability) +totalProbabilities.get(temp.getLanguage()),false);
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









