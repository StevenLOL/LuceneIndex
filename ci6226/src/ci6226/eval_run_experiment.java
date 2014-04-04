/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ci6226;

/**
 *
 * @author steven
 */
import java.util.LinkedList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class eval_run_experiment {
    public static void main(String[] args) throws Exception {
                List<String> lsIds=new LinkedList<String>();
		List<Analyzer> lsAnalyzers = new LinkedList<Analyzer>();
                String[] lsTerms={"to","123","impressed","Geezer","geezer","semi-busy","gs veget","spaghetti with meatballs","\"spaghetti with meatballs\"","cinnamon rolls","\"cinnamon rolls\"","\":(\""};//,"good","I","but","coffee"};
                String reviewFileLocation="/home/steven/Desktop/assiment/yelpdata/yelp_training_set/yelp_training_set_review.json";
                experimentSetup(lsIds,lsAnalyzers);
                int topn=10;
                for (int i=0;i<lsIds.size();i++){
                    eval_index_writer iw=new eval_index_writer(lsAnalyzers.get(i),reviewFileLocation,lsIds.get(i));
                    eval_index_reader ir= new eval_index_reader(lsAnalyzers.get(i),lsIds.get(i),lsTerms,topn);    
                    System.out.println(lsIds.get(i));
                }
	}
    public static void experimentSetup(List<String> lsids,List<Analyzer> lsanalyzers){
        
        
        
        // use white space to separate words   -- simpliest case
        lsids.add("WhitespaceAnalyzer"); 
        lsanalyzers.add(new WhitespaceAnalyzer(Version.LUCENE_47));
        
        // lowercase, remove stop words, stemming, punctuations
        lsids.add("StandardAnalyzer");
        lsanalyzers.add(new StandardAnalyzer(Version.LUCENE_47));
        
        // remove stop words
        lsids.add("StopWordsAnalyzer");
        lsanalyzers.add(new StopWordsAnalyzer());
        
        // lowercase
        lsids.add("LowcaseAnalyzer");
        lsanalyzers.add(new LowcaseAnalyzer());
        
        // stemming
        lsids.add("StemmingAnalyzer");
        lsanalyzers.add(new StemmingAnalyzer());
        
        // remove punctuations
        lsids.add("PunctuationAnalyzer");
        lsanalyzers.add(new PunctuationAnalyzer());
        
        // remove numbers
        lsids.add("NumberAnalyzer");
        lsanalyzers.add(new NumberAnalyzer());
        
        // bi-words
        lsids.add("NGramAnalyzer");
        lsanalyzers.add(new NGramAnalyzer(2,2));
        
     //    lsids.add("EnglishAnalyzer");
     //   lsanalyzers.add(new EnglishAnalyzer(Version.LUCENE_47));
     //    lsids.add("NGramAnalyzer");
     //   lsanalyzers.add(new NGramAnalyzer(2,2));
     //    lsids.add("StandardAnalyzer");
     
    }
}
