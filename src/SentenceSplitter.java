import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.Span;


public class SentenceSplitter {
	
	public static List<List<String> > resultSentence;
	static Set<String> nounPhrases = new HashSet<>();
	static Set<String> pronounPhrases = new HashSet<>();
	static Set<String> articlePhrases = new HashSet<>();
	static Set<String> prepositionPhrases = new HashSet<>();
	static Set<String> adjectivePhrases = new HashSet<>();
	static Set<String> verbPhrases = new HashSet<>();
	static Set<String> conjunctionPhrases = new HashSet<>();
	private POSModel model;
	private POSTaggerME tagger;
	private InputStream is;
	private ChunkerModel cModel;
	private ChunkerME chunkerME;
	
	public SentenceSplitter()
	{
		model = new POSModelLoader().load(new File("en-pos-maxent.bin"));
		tagger = new POSTaggerME(model);

		try
		{
			is = new FileInputStream("en-chunker.bin");
			cModel = new ChunkerModel(is);
			chunkerME = new ChunkerME(cModel);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void getPhrases(Parse p) {
	    if (p.getType().equals("NN") || p.getType().equals("NNS") || p.getType().equals("NNP") || p.getType().equals("NNPS")) {
	        if(p.getType().equals("NNP")) 
	        	nounPhrases.add(p.getCoveredText());
	        else pronounPhrases.add(p.getCoveredText());
	         //System.out.println(p.getCoveredText());
	    }
	    
	    if(p.getType().equals("PRP"))
	    	pronounPhrases.add(p.getCoveredText());
	    
	    if (p.getType().equals("JJ") || p.getType().equals("JJR") || p.getType().equals("JJS")) {
	    	adjectivePhrases.add(p.getCoveredText());
	         //System.out.println(p.getCoveredText());
	    }
	    
	    if (p.getType().equals("VBZ") || p.getType().equals("VB") || p.getType().equals("VBP") || p.getType().equals("VBG")|| p.getType().equals("VBD") || p.getType().equals("VBN")) {
	    	verbPhrases.add(p.getCoveredText());
	         //System.out.println(p.getCoveredText());
	    }
	    
	    if(p.getType().equals("CC"))
	    {
	    	conjunctionPhrases.add(p.getCoveredText());
	    }
	    
	    if(p.getType().equals("DT"))
	    {
	    	articlePhrases.add(p.getCoveredText());
	    }
	    
	    if(p.getType().equals("TO") || p.getType().equals("IN"))
	    {
	    	prepositionPhrases.add(p.getCoveredText());
	    }
	    
	    for (Parse child : p.getChildren()) {
	         getPhrases(child);
	    }
	}
	
	public static void parseSentence(String sentence) throws Exception
	{
		InputStream is = new FileInputStream("en-parser-chunking.bin");
	    ParserModel model = new ParserModel(is);
	    Parser parser = ParserFactory.create(model);
	    
	    clearHashsets();
	    
	    
	    Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);
		for (Parse p : topParses){
		    	//p.show();
		    getPhrases(p);
		}
		    
	    	
	}
	    
	
	
	public static void clearHashsets()
	{
		nounPhrases.clear();
		pronounPhrases.clear();
		articlePhrases.clear();
		prepositionPhrases.clear();
		adjectivePhrases.clear();
		verbPhrases.clear();
		conjunctionPhrases.clear(); 
	}
	
	public static boolean isArticle(String s)
	{
		if(articlePhrases.contains(s))
			return true;
		return false;
	}
	
	public  List<List<String> > splitSentence(List<String> s)
	{
		try {
			split(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return resultSentence;
	}
	
	public void split(List<String> sentence) throws Exception
	{
		resultSentence=new ArrayList<List<String> >();
		for(int i=0;i<sentence.size();i++)
		{
			String input = sentence.get(i);
			parseSentence(input);
			
			ObjectStream<String> lineStream = new PlainTextByLineStream(
					new StringReader(input));
			
			String line;
			String tokens[] = null;
			
			String[] tags = null;
			while ((line = lineStream.read()) != null) {
				tokens = WhitespaceTokenizer.INSTANCE
						.tokenize(line);
				tags = tagger.tag(tokens);
			
				POSSample sample = new POSSample(tokens, tags);
				System.out.println(line);
					
			}
				
			// chunker
			
			List<String> temp=new ArrayList<String>();
			
			Span[] span = chunkerME.chunkAsSpans(tokens, tags);
			int j=0;
			
			for (Span s : span)
			{
				String p="";
				
				for( j=s.getStart();j<s.getEnd();j++)
				{
					if(!p.equals(""))
						p+=" ";
					p+=tokens[j];
				}
				temp.add(p);
//				System.out.println(s.toString());
			}
			if(j<tokens.length)
			{
				temp.add(tokens[j]);
			}
			
			removeArticlesAddResult(temp);
//			resultSentence.add(temp);
		}
			    
	}
	
	public static void removeArticlesAddResult(List<String> sentence)
	{
		List<String> temp=new ArrayList<String>();
		String str,strTemp="";
		
		for(int i=0;i<sentence.size();i++)
		{
			str=sentence.get(i);
//			System.out.println(str);
			String[] tokens=str.split(" ");
			strTemp="";
			
			for(int j=0;j<tokens.length;j++)
			{
				if(isArticle(tokens[j]) && (tokens[j].toLowerCase().equals("an") || tokens[j].toLowerCase().equals("a")))
				{
					//do nothing
				}
				else
				{
					if(!strTemp.equals(""))
						strTemp+=" ";
					strTemp+=tokens[j];
				}
					
			}
			if(!strTemp.equals(""))
				temp.add(strTemp);
		}
		
		resultSentence.add(temp);
	}
	
	public static void print()
	{
		for(int i=0;i<resultSentence.size();i++)
		{
			List<String> temp=resultSentence.get(i);
			System.out.println();
			System.out.println("Index: "+i);
			for(int j=0;j<temp.size();j++)
				System.out.println(temp.get(j));
		}
	}
	
	
	public static void main(String[] args)
	{
		SentenceSplitter s=new SentenceSplitter();
		
		List<String> sen = new ArrayList<String>();
		sen.add("John is a good boy");
		sen.add("All tigers are cat");
		sen.add("John went to college today and John met with a girl named Alisa");
		sen.add("John and Alisa are both classmates");
		sen.add("John and Alisa love each other");
		
		try {
			s.splitSentence(sen);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
//		System.out.println("List of Noun Parse : "+nounPhrases);
//		System.out.println("List of proNoun Parse : "+pronounPhrases);
//		System.out.println("List of Adjective Parse : "+adjectivePhrases);
//		System.out.println("List of Verb Parse : "+verbPhrases);
//		System.out.println("List of conjunction Parse : "+conjunctionPhrases);
//		System.out.println("List of preposition Parse : "+prepositionPhrases);
//		System.out.println("List of Article Parse : "+articlePhrases);
		
		print();
	}
	
}
