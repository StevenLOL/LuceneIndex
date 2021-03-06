/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ci6226;

/**
 * erf :http://ryadyo.wordpress.com/2012/12/18/ngram-tokenizer-using-lucene-4x/
 * 
 * @author steven
 */
 import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

public class NGramAnalyzer extends Analyzer {

  private int minGram;
  private int maxGram;

  public NGramAnalyzer(int minGram, int maxGram) {
    this.minGram = minGram;
    this.maxGram = maxGram;
  }

  
  
  @Override
  protected TokenStreamComponents createComponents(String arg0, Reader reader) {

    Tokenizer source = new StandardTokenizer(Version.LUCENE_47, reader);
    
    TokenStream filter = new ShingleFilter(source, minGram, maxGram);
  //  filter = new LowerCaseFilter(Version.LUCENE_47, filter);
  //  filter = new StopFilter(Version.LUCENE_47, filter,
  //      StopAnalyzer.ENGLISH_STOP_WORDS_SET);

    return new TokenStreamComponents(source, filter);
  }
}