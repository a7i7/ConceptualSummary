import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

public class ConceptualSummarizer {
	private String text;
	private String title;
	private WordList wordList;
	private List<String> sentences;
	private List<String> titleWords;
	
	private static final double TITLE_SCORE_WEIGHT = 1.5;
	private static final double SENTENCE_LENGTH_SCORE_WEIGHT = 0.25;
	private static final double SENTENCE_POSITION_SCORE_WEIGHT = 1.0;
	private static final double DBS_SBS_SCORE_WEIGHT = 2.0;
	private static final double TOTAL_WEIGHT = TITLE_SCORE_WEIGHT
			+ SENTENCE_LENGTH_SCORE_WEIGHT + SENTENCE_POSITION_SCORE_WEIGHT
			+ DBS_SBS_SCORE_WEIGHT;

	ConceptualSummarizer(String text,String title)
	{
		this.text = text;
		this.title = title;
		wordList = new WordList(text);
		sentences = TextSplitter.splitIntoSentences(this.text);
		titleWords = TextSplitter.splitIntoWords(this.title);
		System.out.println(titleWords);
	}
	
	private List<SimpleEntry<String,Integer>> scoreSentences(final Map<String,Double> sentenceRanks)
	{
		List<SimpleEntry<String,Integer>> sortedSentenceListWithPosition = new ArrayList<SimpleEntry<String,Integer>>();
		
		for(int i = 0;i<sentences.size();i++)
		{
			System.out.println(sentences.get(i));
			
			List<String> splittedWords = TextSplitter.splitIntoWords(sentences
					.get(i));
			
			double titleScore = SentenceScoreCalculator.calculateTitleScore(
					titleWords, splittedWords, wordList);
			double sentenceLengthScore = SentenceScoreCalculator
					.sentenceLengthScore(splittedWords);
			double sentencePositionScore = SentenceScoreCalculator
					.sentencePositionScore(i + 1, sentences.size());
			double sbsScore = SentenceScoreCalculator.summationBasedSelection(
					splittedWords, wordList);
			double dbsScore = SentenceScoreCalculator.densityBasedSelection(
					splittedWords, wordList);
			double dbsSbsScore = (sbsScore + dbsScore) * 1.0;
			
			System.out.println(titleScore + " " + sentenceLengthScore + " "
					+ sentencePositionScore + " " + sbsScore + " " + dbsScore);
			
			double totalScore = TITLE_SCORE_WEIGHT * titleScore
					+ DBS_SBS_SCORE_WEIGHT * dbsSbsScore
					+ SENTENCE_LENGTH_SCORE_WEIGHT * sentenceLengthScore
					+ SENTENCE_POSITION_SCORE_WEIGHT * sentencePositionScore;
			totalScore /= TOTAL_WEIGHT;

			sentenceRanks.put(sentences.get(i), totalScore);
			sortedSentenceListWithPosition
					.add(new SimpleEntry<String, Integer>(sentences.get(i), i));
			System.out.println(totalScore);
			System.out.println("~~~~~~~~~~~~~~~");
		}
		
		Collections.sort(sortedSentenceListWithPosition, new Comparator<SimpleEntry<String, Integer>>() {
		    @Override
		    public int compare(SimpleEntry<String, Integer> x, SimpleEntry<String, Integer> y) {
		        if(sentenceRanks.get(y.getKey()) < sentenceRanks.get(x.getKey()))
		        	return -1;
		        if(sentenceRanks.get(y.getKey()) > sentenceRanks.get(x.getKey()))
		        	return +1;
		        return 0;
		    }
		});
		
		return sortedSentenceListWithPosition;
	}
	
	public List<String> getSummary()
	{
		final Map<String,Double> sentenceRanks = new HashMap<String,Double>();
		List<SimpleEntry<String,Integer>> rankedSentences =  scoreSentences(sentenceRanks);
		List<SimpleEntry<String,Integer>> intermediateSentences = new ArrayList<SimpleEntry<String,Integer>>();
		List<String> finalSummary = new ArrayList<String>();
		
		for(SimpleEntry<String,Integer> s: rankedSentences)
		{
			System.out.println(s.getKey());
			System.out.println(sentenceRanks.get(s.getKey()));
		}
		
		for(SimpleEntry<String,Integer> s: rankedSentences)
		{
			intermediateSentences.add(s);
			if(intermediateSentences.size()==4)
				break;
		}
		
		Collections.sort(intermediateSentences, new Comparator<SimpleEntry<String, Integer>>() {
		    @Override
		    public int compare(SimpleEntry<String, Integer> x, SimpleEntry<String, Integer> y) {
		        if(y.getValue() > x.getValue())
		        	return -1;
		        if(y.getValue() < x.getValue())
		        	return +1;
		        return 0;
		    }
		});
		
		for(SimpleEntry<String,Integer> s:intermediateSentences)
		{
			System.out.println(s.getKey()+"\n"+s.getValue());
			finalSummary.add(s.getKey());
		}
		return finalSummary;
	}
}
