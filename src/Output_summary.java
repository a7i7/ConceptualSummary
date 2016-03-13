
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
// package output_summary;

/**
 *
 * @author bolt
 */

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.*;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import java.io.StringReader;
import java.util.Collection;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class Output_summary {

    /**
     * @param args the command line arguments
     */
    
    
    //some of below declarations used in "ner" function which give us person tag word
    private Properties prop;
	private StanfordCoreNLP pipeline;
        LexicalizedParser lp = LexicalizedParser.loadModel("./library/englishPCFG.ser.gz");
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf= tlp.grammaticalStructureFactory();
//	public String Sentence_boundary(String text){
//            String output="";
//            Annotation document = new Annotation(text);
//	    // run all Annotators on this text
//	    pipeline.annotate(document);
//	    // these are all the sentences in this document
//	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
//	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
//	    for(CoreMap sentence: sentences) {
//                output+=sentence+" xxx ";
//            }
//            return output;
//        }

	// no need to understand use for standford parser
	public Output_summary(){
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	    prop = new Properties();
	    try{
	    	prop.put("annotators","tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    	prop.put("ssplit.eolonly",true);
	    	pipeline = new StanfordCoreNLP(prop);
	    }
	    catch(Exception e){System.out.println(e);}
	}
	public String postag(String text){
            //System.out.println("inside POS Tag");
            String output = "";
            // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	    	// this is the text of the token
	        String word = token.get(TextAnnotation.class);
	        // this is the POS tag of the token
	        String pos = token.get(PartOfSpeechAnnotation.class);
	        //this is the lemma annotation
	        String lemma = token.get(LemmaAnnotation.class);
	        //output+= word+"/"+lemma+"/"+pos+" ";
                output+= word+"/"+lemma+"/"+pos+" ";
	      }
	      //output+="\n";
	    }
	    return output;
	}
        //below fun ner uses for tagging the person name
        //return string of all person names occur in a single sentence except first person if sentence starts with a person name 
        public String ner(String text){
            //System.out.println("inside POS Tag");
            String output = "";
            // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(TextAnnotation.class);
                //this is named entity annotation of token
                String ner = token.getString(NamedEntityTagAnnotation.class);
                if(ner.equals("PERSON"))
	        output+= word+" ";
	      }
              //if(!output.equals(""))
	      //output+="\n";
	    }
          //  System.out.println(output+" "+output.length());
	    return output;
	}
	//below fn not any use
	
        public String plain_dependency(String text){
            String output = "";
            // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
            for(CoreMap sentence: sentences) {
                //Tree tree = sentence.get(TreeAnnotation.class);
                SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
                output += dependencies.toString();
                System.out.println(output);
            }
            return output;
        }
		
		//below function  dependency  gives dependency graph not any use bt nt comment out may give errors
        public String dependecy(String text){
            String output = "";
            // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    for(CoreMap sentence: sentences) {
                TokenizerFactory tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
                List wordList = tokenizerFactory.getTokenizer(new StringReader(sentence.toString())).tokenize();
                Tree tree = lp.apply(wordList);
                //String set = sentence.toString();
                //String[] ss = set.split(" ");
                //List<CoreLabel> rawWords = Sentence.toCoreLabelList(ss);
//                // this is the parse tree of the current sentence
                //Tree tree =  sentence.get(TreeAnnotation.class);
                //SemanticGraph dependecy = sentence.get(BasicDependenciesAnnotation.class);
//                
                GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
                Collection cc = gs.typedDependenciesCCprocessed(true);
                output +=cc.toString();
            }
            
            return output;
        }
		
		//below function  find_tree  vgives parse tree not any use bt nt comment out may give errors
        public String find_tree(String text){
            String output = "";
            // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    for(CoreMap sentence: sentences) {
                TokenizerFactory tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
                List<String> wordList = tokenizerFactory.getTokenizer(new StringReader(sentence.toString())).tokenize();
                //Tree tree = lp.apply(wordList);
                //String set = sentence.toString();
                //String[] ss = set.split(" ");
                //List<CoreLabel> rawWords = Sentence.toCoreLabelList(ss);
//                // this is the parse tree of the current sentence
                Tree tree =  sentence.get(TreeAnnotation.class);
//                
                //GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
                //Collection cc = gs.typedDependenciesCCprocessed(true);
                output +=tree.toString();
            }
            
            return output;
        }
    
    //Output_summary global_os=new Output_summary();
        
        
        
    
    public List<String> createSentenceDiagram(List<List<String>> partedSentence){
        Output_summary global_os=new Output_summary();
        ArrayList<String> removable_words=new ArrayList<String>();
        BufferedReader br;
        try{
            br =new BufferedReader(new FileReader("stop_word.txt"));  //stop_word contain unnecessary preposition,article,
            String scan;
            while((scan=br.readLine())!=null){
                removable_words.add(scan.trim());           //store stop word in arraylist
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
//        for(int i=0;i<removable_words.size();i++){
//            System.out.println(removable_words.get(i));
//        }
        List<String> output=new ArrayList<String>();
        
        for(int i=0;i<partedSentence.size();i++){
            List<String> temp=new ArrayList<String>();
            temp=partedSentence.get(i);
            String prefinal_sentence="";
			
			//below loop just take a single sentence and send it to function "ner" to get person tagged word
            for(int j=0;j<temp.size();j++){
                //System.out.println(temp.get(j)+"**");
                
                if(prefinal_sentence.equals(""))
                prefinal_sentence=prefinal_sentence+temp.get(j);
                else
                prefinal_sentence=prefinal_sentence+" "+temp.get(j);
            }
          //  System.out.println(prefinal_sentence+"******");
            String findperson = global_os.ner(prefinal_sentence);
            
			
			//if the sentence contain person tagged word,then we check for same person dependecy and if found then remove it,after this process done we thenalso remove stop word.
            if(findperson.length()!=0){
                //System.out.println(prefinal_sentence+"******");
                String[] array;
                array=prefinal_sentence.split(" ");
                //System.out.println(findperson);
                String[] noun_person=findperson.split(" "); //store person name
                //remove duplicate occurance of person
                String correct_input="";
                
                    //System.out.println(noun_person[yy]);
                   
                   
                    Set<String>set=new HashSet<String>();
                    
                    for(int pp=0;pp<temp.size();pp++){
                        int counter=0;
                        for(int yy=0;yy<noun_person.length;yy++){
                            String name=noun_person[yy];
                            if(temp.get(pp).equals(name) && set.contains(name))
                            {
                                counter=1;
                                break;
                            }
                            if(temp.get(pp).contains(name))
                            {
                                set.add(name);
                                break;
                            } 
                        
                        }
						
						// counter=0 means no duplicate same person,now we remove stop word
                        if(counter==0){
                            int binary=0;
                            for(int iter=0;iter<removable_words.size();iter++){
                                if(temp.get(pp).equals(removable_words.get(iter))){
                                    binary=1;
                                    break;
                                }
                            }
                            if(binary==1)
                                continue;
                            if(correct_input.equals(""))
                        correct_input=correct_input+temp.get(pp);
                        else
                        correct_input=correct_input+"-->"+temp.get(pp);
                            
                        }
                        
                        
                    }
                  //  System.out.println(correct_input+"********");
//                    String[] ans=correct_input.split(" ");
//                    String correct_output="";
//                    for(int it=0;it<ans.length;it++){
//                       int binary=0;
//                        for(int iter=0;iter<removable_words.size();iter++){
//                            if(ans[it].equals(removable_words.get(iter))){
//                                binary=1;
//                                break;
//                            }
//                        }
//                        if(binary==1)
//                            continue;
//                        if(correct_output.equals(""))
//                        correct_output=correct_output+ans[it];
//                        else
//                        correct_output=correct_output+"-->"+ans[it];
//                    }
                    output.add(correct_input);
                    //System.out.println(correct_output);
            } //endif
         
         
           //if no person tagged word then it comes to else ,now only remove stop words.
            else{
                //List<String> temp=new ArrayList<String>();
            temp=partedSentence.get(i);
             prefinal_sentence="";
            for(int j=0;j<temp.size();j++){
                //System.out.println(temp.get(j)+"**");
                String str=temp.get(j);
                int flag=0;
                for(int k=0;k<removable_words.size();k++){
                    //System.out.println(removable_words.get(k));
                    if(str.equals(removable_words.get(k))){
            //            System.out.println(str+"**"+removable_words.get(k));
                        flag=1;
                        break;
                    }
                }
                if(flag==1)
                    continue;
                if(prefinal_sentence.equals(""))
                prefinal_sentence=prefinal_sentence+str;
                else
                prefinal_sentence=prefinal_sentence+"-->"+str;
            }
            output.add(prefinal_sentence);
            //System.out.println(prefinal_sentence);
            }
        }
        return output;
    }
    public static void main(String[] args) {
        // TODO code application logic here
        Output_summary os=new Output_summary();
        List< List<String> > paragraph=new ArrayList< List<String> >();
        List<String> sentence1=new ArrayList<String>();
        List<String> sentence2=new ArrayList<String>();
        List<String> sentence3=new ArrayList<String>();
        List<String> sentence4=new ArrayList<String>();
        List<String> sentence5=new ArrayList<String>();
        
//        
//        try{
//             br=new BufferedReader(new FileReader("input_paragraph.txt"));
//             String scan;
//             while((scan=br.readLine())!=null){
//                 System.out.println(scan);
//                 int flag=0;
//                 char temp;
////                 for(int i=0;i<scan.length();i++){
////                     if(temp==' ' && flag==0){
////                 
////                }
//                 String[] tokn;
//                 tokn=scan.split("[^\"]\\s*+");
//                 for(int i=0;i<tokn.length;i++){
//                     System.out.println(tokn[i]+">>>"+i);
//                 }
//             }
//             br.close();    
//        }
//       catch(Exception e){
//           e.printStackTrace();
//       }
        sentence1.add("john");
        sentence1.add("is");
        sentence1.add("a");
        sentence1.add("good boy");
        paragraph.add(sentence1);
        //sentence.removeAll();
        sentence2.add("john");
        sentence2.add("is");
        sentence2.add("very bad");
        paragraph.add(sentence2);
        
        sentence3.add("john");
        sentence3.add("went");
        sentence3.add("to");
        sentence3.add("college today");
        sentence3.add("and");
        sentence3.add("john");
        sentence3.add("met");
        sentence3.add("a");
        sentence3.add("girl named Alisa");
        paragraph.add(sentence3);
        
        sentence4.add("john and Alisa");
        sentence4.add("are");
        sentence4.add("both");
        sentence4.add("classmates");
        paragraph.add(sentence4);
        //sentence.clear();
        sentence5.add("john and Alisa");
        sentence5.add("love");
        sentence5.add("each other");
        paragraph.add(sentence5);
        
        List<String> op=new ArrayList<String>();
        op=os.createSentenceDiagram(paragraph);  //op store final required output
        for(int jj=0;jj<op.size();jj++){
            System.out.println(op.get(jj));
        }
        //String myexample = os.ner("john went to college today and john met a girl named Alisa");
        //System.out.println(myexample);
        
    }
}