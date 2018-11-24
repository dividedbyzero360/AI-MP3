package MiniProject3;
import java.util.ArrayList;

public class PlayGround {
	
	
	public static void main(String[] args) {
		String textString=FileHandler.getString("en-moby-dick.txt");
		ArrayList<Character> textCharList=CleanFile.getAllCharactersInTheString(textString);
		System.out.println(textCharList.size());

	}
	
	
	
	

}
