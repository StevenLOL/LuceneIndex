/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ci6226;

import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

/**
 *
 * @author xiaohai
 */
public class CustormizedAnalyzer extends Analyzer {
    CustormizedAnalyzer() {
    }
    
    @Override
    protected CustormizedAnalyzer.TokenStreamComponents createComponents(String arg0, Reader reader) 
    {
        TokenStream source = new StandardTokenizer(Version.LUCENE_47, reader);
        
        TokenStream filter = new LowerCaseFilter(Version.LUCENE_47, source);
        filter = new PorterStemFilter(filter);
        filter = new StopFilter(Version.LUCENE_47, filter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
                   
        return new Analyzer.TokenStreamComponents((Tokenizer) source, filter);
    }    
}
