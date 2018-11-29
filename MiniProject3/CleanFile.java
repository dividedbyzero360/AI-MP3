package MiniProject3;
import java.util.*;

public class CleanFile {	
	private static List<Character> characters=Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't'
			, 'u', 'v', 'w', 'x', 'y', 'z');
	
	
	
	
	
	public static ArrayList<Character> getAllCharactersInTheString(String s)
	{
		ArrayList<Character> textCharList=new ArrayList<>();
		String textString=getCleanString(s);
		for(int i=0; i< textString.length();i++)
		{
			if(textString.charAt(i)!='+' && !characters.contains(textString.charAt(i)))
			{
				throw new RuntimeException("Not possible "+textString.charAt(i));
			}
			textCharList.add(textString.charAt(i));
		}
		return textCharList;
	}
	
	
	public static String getCleanString(String s)
	{
		//different for french and english
	    //s=s.replace("'","").replaceAll("’","") ;	
		//s=s.toLowerCase().replaceAll(System.getProperty("line.separator"), " ").replaceAll("[^A-Za-z ]", " ").replaceAll("\\s+", "+");
		s=s.toLowerCase().replaceAll(System.getProperty("line.separator"), " ").replaceAll("[^A-Za-z ]", " ").replaceAll("\\s+", "");
		//FileHandler.writeToFile(s);
		return s;
	}
}




//s=s.replaceAll(System.getProperty("line.separator"), " ");
//s=s.replaceAll("[^A-Za-z ]", " ").replaceAll("\\s+", "+");