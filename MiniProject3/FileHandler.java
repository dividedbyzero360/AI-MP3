package MiniProject3;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
//https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java
public class FileHandler {
	public static String getString(String fileName) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			return everything;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public static ArrayList<String> getSentences(String fileName)
	{
		ArrayList<String> sentences=new ArrayList<String>();
		BufferedReader br = null;
		String line="";
		try {
			br = new BufferedReader(new FileReader(fileName));
			while((line=br.readLine())!=null)
			{
				sentences.add(line);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sentences;
	}
	
	public static void writeToFile(String line)
	{
		BufferedWriter wr=null;
		try {
			 wr=new BufferedWriter(new FileWriter("output.txt"));
			 wr.write(line);
			 wr.flush();
			 wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
