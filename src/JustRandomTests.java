import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JustRandomTests {
	
	public static String readString(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		String data = "";
		String line = null;
		while((line=br.readLine())!=null)
		{
			line+="\n";
			data+=line;
		}
		data = data.trim();
		br.close();
		return data;
	}
	public static void main(String args[]) throws IOException
	{
		final String TITLE = "SOFT WORMBOT TRIES OUT NEW LIGHT-UP SKIN.txt";
		String text = readString("./test/"+TITLE);
		ConceptualSummarizer c = new ConceptualSummarizer(text,TITLE.replace(".txt", ""));
		List<String> summary = c.getSummary();
		for(String s: summary)
			System.out.println(s);
//		System.exit(0);
//		List<String> sentences = TextSplitter.splitIntoSentences(text);
//		List<String> titleWords = TextSplitter.splitIntoWords("Percheron");
//		WordList wordList = new WordList(text);
//		int position = 0;
//		for(String s:sentences)
//		{
//			System.out.println(s);
//			List<String> words = TextSplitter.splitIntoWords(s);
////			System.out.println(SentenceScoreCalculator.calculateTitleScore(titleWords, words, wordList));
////			System.out.println(SentenceScoreCalculator.sentenceLengthScore(words));
////			System.out.println(SentenceScoreCalculator.sentencePositionScore(position+1, sentences.size()));
//			System.out.println(SentenceScoreCalculator.dbs(words, wordList));
//			++position;
//		}
		
//		System.out.println(wordList.getKeyWords());
//		
//		for(String s: TextSplitter.splitIntoSentences(text))
//			System.out.println(s+"\n");
	}
}