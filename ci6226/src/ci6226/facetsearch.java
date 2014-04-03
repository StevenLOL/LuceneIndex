/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ci6226;

import static ci6226.loadIndex.ANSI_BLUE;
import static ci6226.loadIndex.ANSI_RESET;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
/**
 *
 * @author steven
 */
public class facetsearch {
    public static void main(String[] args) throws Exception {
          String index = "./myindex";
        String field = "text";
        String queries = null;
        int hitsPerPage = 10;
        boolean raw = false;
        
        //http://lucene.apache.org/core/4_0_0/facet/org/apache/lucene/facet/doc-files/userguide.html#facet_accumulation
        
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        // :Post-Release-Update-Version.LUCENE_XY:
        
        //TODO: SAME AS HOW U BUILD INDEX
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
        BufferedReader in = null;
        if (queries != null) {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), "UTF-8"));
        } else {
            in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        }
        // :Post-Release-Update-Version.LUCENE_XY:
        QueryParser parser = new QueryParser(Version.LUCENE_47, field, analyzer);
        while (true) {

            System.out.println("Enter query: ");
            String line = in.readLine();
            line = line.trim();
            if (line.length() == 0) {
                break;
            }
            Query query = parser.parse(line);
            System.out.println("Searching for: " + query.toString(field));
            Date start = new Date();
            searcher.search(query, null, 100);
            Date end = new Date();
            System.out.println("Time: " + (end.getTime() - start.getTime()) + "ms");         
            TopDocs results = searcher.search(query, 5 * hitsPerPage);
            ScoreDoc[] hits = results.scoreDocs;
            int numTotalHits = results.totalHits;
            
            //N= max docs
            //df = totoal matched doc
            //idf=log(N/df)
            
            
            for(int i=0;i<hits.length;i++){
                Document doc = searcher.doc(hits[i].doc);
                System.out.println(ANSI_BLUE+(i + 1) + ANSI_RESET+"\nScore=\t" + hits[i].score);
                String rtext = doc.get(field);                
                System.out.println("Text=\t"+rtext);
               
                  Terms vector = reader.getTermVector(i, "text");
                  if (vector == null)  
                    continue;   
                 // System.out.println(vector.getSumDocFreq());
                
               // Terms vector = reader.getTermVector(hits[i].doc, field);  //hits[i].doc=docID
TermsEnum termsEnum = vector.iterator(null);
termsEnum = vector.iterator(termsEnum);
Map<String, Integer> frequencies = new HashMap<>();
BytesRef text = null;
while ((text = termsEnum.next()) != null) {
    String term = text.utf8ToString();
    int freq = (int) termsEnum.totalTermFreq();
    frequencies.put(term, freq);
   // System.out.println("Time: "+term + " idef "+freq);
}
                
              
            }
     
         //   String[] facetCatlog={""};
         
            System.out.println(numTotalHits + " total matching documents");

        }

        reader.close();
    }
}
