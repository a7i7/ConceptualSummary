import java.util.Iterator;
import java.util.List;


public class SentenceScoreCalculator {
	
	public final static int IDEAL_SENTENCE_LENGTH = 20;
	
	public static double calculateTitleScore(List<String> titleWords,List<String> sentence,WordList wordList)
	{
		for(Iterator<String> it = titleWords.iterator();it.hasNext();)
		{
			String word = it.next();
			if(wordList.getStopWords().contains(word))
				it.remove();
		}
		
		int count = 0;
		for(String word:sentence)
		{
			if(titleWords.contains(word) && !wordList.getStopWords().contains(word))
				++count;
		}
		
		if(titleWords.size()==0)
			return 0.0;
		
		return count/(double)titleWords.size();
	}
	
	public static double sentenceLengthScore(List<String> sentence)
	{
		double absDiff = Math.abs(sentence.size()-IDEAL_SENTENCE_LENGTH);
		return 1.0 - (absDiff/IDEAL_SENTENCE_LENGTH);
	}
	
	//http://research.nii.ac.jp/ntcir/workshop/OnlineProceedings3/NTCIR3-TSC-SekiY.pdf (illegal though)
	public static double sentencePositionScore(int position,int totalSentences)
	{
		double normalizedPosition = position/(double)totalSentences;
		if(normalizedPosition>0 && normalizedPosition<=0.1)
	        return 0.17;
		else if(normalizedPosition>0.1 && normalizedPosition<=0.2)
	        return 0.23;
	    else if(normalizedPosition>0.2 && normalizedPosition<=0.3)
	        return 0.14;
	    else if(normalizedPosition>0.3 && normalizedPosition<=0.4)
	        return 0.08;
	    else if(normalizedPosition>0.4 && normalizedPosition<=0.5)
	        return 0.05;
	    else if(normalizedPosition>0.5 && normalizedPosition<=0.6)
	        return 0.04;
	    else if(normalizedPosition>0.6 && normalizedPosition<=0.7)
	        return 0.06;
	    else if(normalizedPosition>0.7 && normalizedPosition<=0.8)
	        return 0.04;
	    else if(normalizedPosition>0.8 && normalizedPosition<=0.9)
	        return 0.04;
	    else if(normalizedPosition>0.9 && normalizedPosition<=1.0)
	        return 0.15;
	    else
	        return 0.0;
	}
	
	public static double summationBasedSelection(List<String> sentence,WordList wordList)
	{
		double score = 0.0;
		if(sentence.size()==0)
			return 0.0;
		for(String word:sentence)
			if(wordList.getKeyWords().get(word)!=null)
			{
				score+=wordList.getKeyWords().get(word);
			}
		return (score/Math.abs(sentence.size()))/1.0;
	}
	
	public static double densityBasedSelection(List<String> sentence,WordList wordList)
	{
		double score;
		if(sentence.size()==0)
			return 0.0;
		double sum = 0.0;
		int firstIndex = -1;
		int secondIndex = -1;
		double firstScore = 0.0,secondScore;
		
		for(int i=0;i<sentence.size();i++)
		{
			String word = sentence.get(i);
			if(wordList.getKeyWords().containsKey(word))
			{
				score = wordList.getKeyWords().get(word);
				if(firstIndex==-1)
				{
					firstIndex = i;
					firstScore = score;
				}
				else
				{
					secondIndex = firstIndex;
					secondScore = firstScore;
					firstIndex = i;
					firstScore = score;
					int dif = firstIndex-secondIndex;
					sum+=(firstScore*secondScore)/(Math.pow(dif, 2));
				}
			}
		}
		
		int commonWords = 0;
		for(String s:wordList.getKeyWords().keySet())
			if(sentence.contains(s))
				++commonWords;
		if(commonWords==0)
			return 0.0;
		return (1/(commonWords*(commonWords+1.0))*sum);		
	}
}
