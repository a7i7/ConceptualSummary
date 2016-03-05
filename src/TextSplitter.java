import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TextSplitter {
	
	public static List<String> splitIntoSentences(String str)
	{
		String[] sentences = str.split("(?<=[.?!])\\s+(?=[a-zA-Z])");
		return Arrays.asList(sentences);
	}
	
	public static List<String> splitIntoWords(String str)
	{
		String[] words = str.replaceAll("[^a-zA-Z0-9 ]", " ").toLowerCase().split("\\s+");
		List<String> listOfWords = new ArrayList<String>();
		for(String w:words)
		{
			if(w.length()>1)
				listOfWords.add(w.toLowerCase());
		}
		return listOfWords;
	}
}
