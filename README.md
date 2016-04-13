# ConceptualSummary
#### Generates Conceptual Summary Of a text
---

# Algorithm to generate Conceptual Summary of a text

This task is divided into 3 small tasks

##### Select best 4 sentences

1. Find a frequency table of top 10 most frequent words in text. These words will be known as keywords. Associate a score with each keyword directly proportional to frequency.

2. Use Concept net to assign same score to words similar to these keywords. These similar words will also be used as keywords.

3. Split the text into sentences.

4. Assign Score to each sentence S using the following formula = (2.0*titleScore+0.25*sentenceLengthScore+1.0*sentencePositionScore+2.0*DbsSbsScore)/(2.0+0.25+1.0+2.0)

5. Choose top 4 sentences with highest scores.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
How to calculate Score
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

############################## TitleScore :
count = Number Of common non-stop words between query and sentence  
NOTE: words in sentence that are similar in concept to any word in query are also counted (done with the help of concept net web API)  
titleScore = count/number Of words in query  

############################## SentenceLengthScore:
sentenceLengthScore = 1.0-abs(number of words in sentence-20)/20  
where 20 is ideal sentence length

############################## SentencePositionScore:
n = normalized Position of sentence in between 0 and 1  
The following research paper has the values listed in page 3  
http://research.nii.ac.jp/ntcir/workshop/OnlineProceedings3/NTCIR3-TSC-SekiY.pdf  

############################## SummationBasedSelection:
score = sum of scores of all keywords contained in the sentence calculated in first step  
summationBasedSelection=score/totalWordsInSentence

############################## DensityBasedSelection:
The formula is well written in page 3 of the following paper  
http://www3.ntu.edu.sg/home/axsun/paper/sun_cikm07s.pdf  
DbsSbsScore = summationBasedSelection+densityBasedSelection  

##### Divide the selected sentences into chunks

1. Create POS tree from sentences using open nlp tool.

2. Store nouns, adjectives, pronouns, verb, adverb, adjective, conjuction, preposition in different arraylist.

3. Create chunks from the sentences using ChunkerModel and ChunkerMe of open nlp tool thereby removing unneccessary conjuction.

4. From the POS arraylists remove unneccessary POS if any present from each sentences.

##### Merge those chunks and generate output

1. Remove unnecessary articles and prepositions.
	* Remove all the stop words from every chunk of words.  
	Stop word list used : http://www.ranks.nl/stopwords

2. Coreference resolution using stanford parser.

3. Merge those chunks and generate output.
---

# Creating jar file

### Use these libraries while building source code:

* ejml-0.19-nogui.jar
* ejml-0.19-src.zip
* englishPCFG.ser.gz
* joda-time.jar
* joda-time-2.1-sources.jar
* jollyday.jar
* jollyday-0.4.7-sources.jar
* json-20160212.jar
* wnl-1.3.3.jar
* opennlp-maxent-3.0.3.jar
* opennlp-tools-1.5.3.jar
* opennlp-uima-1.5.3.jar
* RiTaWN.jar
* SentenceSplitter.java
* stanford-corenlp-3.2.0.jar
* stanford-corenlp-3.2.0-javadoc.jar
* stanford-corenlp-3.2.0-models.jar
* stanford-corenlp-3.2.0-sources.jar
* supportWN.jar
* xom.jar
* xom-src-1.2.8.zip


---

# Executing the jar file

1. Use the above mentioned libraries while executing the jar file.
2. In addition, keep these files into "lib" directory
	* ejml-0.19-src.zip
	* en-chunker.bin
	* englishPCFG.ser.gz
	* en-parser-chunking.bin
	* en-pos-maxent.bin
	* xom-src-1.2.8.zip
3. Provide at least 1 GB of RAM while executing the jar file.
	* (Use "-Xmx1024m" argument in addition)


---

# Team members names and contacts

* Afif Ahmed - afif.ahmed@hotmail.com
* Sushanto Halder - snhalder300@gmail.com
* Sourav Maji - srvmaji@gmail.com
* Anit Kumar - kumaranitsingh@gmail.com
* Debraj Dutta - debrajdutta2009@gmail.com
