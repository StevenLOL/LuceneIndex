/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ci6226;

import java.io.Reader;
////import static java.util.Locale.filter;
//import static java.util.Locale.filter;
//import static jdk.nashorn.internal.objects.NativeArray.filter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;
//import static sun.awt.image.ImagingLib.filter;
//import static sun.reflect.annotation.TypeAnnotation.filter;
//import static sun.util.locale.LocaleMatcher.filter;

/**
 *
 * @author xiaohai
 */
public final class myAnalyzer
    extends Analyzer
{



    myAnalyzer() {
        //@Override
        // TokenStreamComponents createComponents(String fieldName, Reader reader) {
         //   return new WhitespaceTokenizer(reader);
        //}
    }

    @Override
    protected myAnalyzer.TokenStreamComponents createComponents(String arg0, Reader reader) 
    {
        TokenStream source = new WhitespaceTokenizer(Version.LUCENE_47, reader);
        
        //TokenStream source = new LetterFilter(Version.LUCENE_47, reader);
        
        TokenStream filter = new LowerCaseFilter(Version.LUCENE_47, source);
        filter = new PorterStemFilter(filter);
        
        //TokenStream filter = new StopFilter(Version.LUCENE_47, source, StopAnalyzer.ENGLISH_STOP_WORDS_SET);

        //ilter = new StandardFilter(Version.LUCENE_47, source);
        //TokenStream filter = new LowerCaseFilter(Version.LUCENE_47, source);
        filter = new StopFilter(Version.LUCENE_47, filter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
        
        return new TokenStreamComponents((Tokenizer) source, filter);
    }
}
