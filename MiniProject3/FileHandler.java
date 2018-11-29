package MiniProject3;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.text.Normalizer;
//https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java
public class FileHandler {
	
	private static BufferedWriter bw;
	private static int line=0;
	
	public static void incrementLineNo()
	{
		FileHandler.line++;
	}
	
	public static void writeSentences( String sentence, boolean printToConsoleAsWell)
	{
		
		if(printToConsoleAsWell)
		{
			System.out.println(sentence);
		}
		try {
			if(bw==null)
			{
				bw=new BufferedWriter(new FileWriter("out"+FileHandler.line+".txt"));
			}		
			bw.write(sentence);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeWriter()
	{
		try{
			if(bw!=null)
			{
				bw.close();
				bw=null;
			}
		}
		catch(IOException ex)
		{
			System.out.println("Error occured while closing the file");
		}
	}
	
	
	
	
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
//			System.out.println(everything);
			everything = Normalizer.normalize(everything, Normalizer.Form.NFD);
//			System.out.println(everything);
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
