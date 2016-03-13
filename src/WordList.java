import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class WordList {
	private Set<String> stopWords;
	private Map<String,Double> keyWords;
	final static int TOP_KEYWORDS_LENGTH = 10;
	private final static boolean CONCEPT_NET_WEB_API_ON = false;
	
	WordList(String text)
	{
		stopWords = new HashSet<String>();
		String stopWordsAsArray[] = {"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"};
		for(String stopWord:stopWordsAsArray)
			stopWords.add(stopWord);
		this.setSentence(text);
	}
	
	public void setSentence(String text)
	{
		List<String> words = TextSplitter.splitIntoWords(text);
		final Map<String,Integer> freqMap = new HashMap<String,Integer>();
		for(String word:words)
		{
			int cnt = 0;
			if(stopWords.contains(word))
				continue;
			
			if(freqMap.get(word)==null)
				cnt = 0;
			else
				cnt = freqMap.get(word);
			freqMap.put(word, cnt+1);
		}
		
		List<String> sortedList = new ArrayList<String>(freqMap.keySet());
		Collections.sort(sortedList, new Comparator<String>() {
		    @Override
		    public int compare(String x, String y) {
		        return freqMap.get(y) - freqMap.get(x);
		    }
		});
		
		keyWords = new HashMap<String,Double>();
		
		for(String s:sortedList)
		{
			
			double score = 1 + (freqMap.get(s)*1.5)/words.size();
			keyWords.put(s, score);
			
			if(keyWords.keySet().size()==TOP_KEYWORDS_LENGTH)
				break;
			
		}
		
		if(CONCEPT_NET_WEB_API_ON)
		{
			List<String> currentKeyset = new ArrayList<String>(keyWords.keySet());
			for(String s:currentKeyset)
			{
				List<String> similarAssociatedWords = null;
				double currentKeyWordScore = keyWords.get(s);
				try
				{
					similarAssociatedWords = ConceptNetDataAccessInterface.getListOfAssociations(s);
				}
				catch(IOException e)
				{
					e.printStackTrace();
					break;
				}
				System.out.println(s);
				System.out.println(similarAssociatedWords);
				for(String word:similarAssociatedWords)
				{
					if(keyWords.get(word)==null || keyWords.get(word)<currentKeyWordScore)
						keyWords.put(word, currentKeyWordScore);
				}
			}
		}
		
	}
	
	public Set<String> getStopWords()
	{
		return stopWords;
	}
	
	public Map<String,Double> getKeyWords()
	{
		return keyWords;
	}
}
