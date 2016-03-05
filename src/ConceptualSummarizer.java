import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConceptualSummarizer {
	private String text;
	private String title;
	private WordList wordList;
	private List<String> sentences;
	private List<String> titleWords;
	
	ConceptualSummarizer(String text,String title)
	{
		this.text = text;
		this.title = title;
		wordList = new WordList(text);
		sentences = TextSplitter.splitIntoSentences(this.text);
		titleWords = TextSplitter.splitIntoWords(this.title);
	}
	
	private List<String> scoreSentences(final Map<String,Double> sentenceRanks)
	{
		
		for(int i = 0;i<sentences.size();i++)
		{
			List<String> splittedWords = TextSplitter.splitIntoWords(sentences.get(i));
			double titleScore = SentenceScoreCalculator.calculateTitleScore(titleWords, splittedWords, wordList);
			double sentenceLengthScore = SentenceScoreCalculator.sentenceLengthScore(splittedWords);
			double sentencePositionScore = SentenceScoreCalculator.sentencePositionScore(i+1, sentences.size());
			double sbsScore = SentenceScoreCalculator.sbs(splittedWords, wordList);
			double dbsScore = SentenceScoreCalculator.dbs(splittedWords, wordList);
			double dbsSbsScore = (sbsScore+dbsScore) / 2.0 *10.0;
			double totalScore = 1.5*titleScore + dbsSbsScore*2.0 + sentenceLengthScore*1.0 + sentencePositionScore*1.0;
			totalScore/= 4.0;
			
			sentenceRanks.put(sentences.get(i), totalScore);
			
//			System.out.println(splittedWords);
//			System.out.println(sentences.get(i));
//			System.out.println(titleScore+" "+sentenceLengthScore+" "+sentencePositionScore+" "+sbsScore+" "+dbsScore);
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
		}
		
		Collections.sort(sentences, new Comparator<String>() {
		    @Override
		    public int compare(String x, String y) {
		        if(sentenceRanks.get(y) < sentenceRanks.get(x))
		        	return -1;
		        if(sentenceRanks.get(y) > sentenceRanks.get(x))
		        	return +1;
		        return 0;
		    }
		});
		
//		for(String s:sentences)
//		{
//			System.out.println(s);
//			System.out.println(sentenceRanks.get(s));
//		}
		return sentences;
//		System.out.println(wordList.getKeyWords().keySet());
	}
	
	public List<String> getSummary()
	{
		final Map<String,Double> sentenceRanks = new HashMap<String,Double>();
		List<String> rankedSentences =  scoreSentences(sentenceRanks);
		List<String> finalSummary = new ArrayList<String>();
		for(String s: rankedSentences)
		{
			finalSummary.add(s);
			if(finalSummary.size()==4)
				break;
		}
		return finalSummary;
	}
}
