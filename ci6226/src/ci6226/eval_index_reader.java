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
import ci6226.NGramAnalyzer;
import static ci6226.loadIndex.ANSI_RED;
import static ci6226.loadIndex.ANSI_RESET;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONValue;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.EnglishStemmer;

public class eval_index_reader {

    public eval_index_reader(Analyzer _analyzer, String _dir, String[] _searchList, int _topn) throws IOException, org.apache.lucene.queryparser.classic.ParseException, InvalidTokenOffsetsException {
        String indexdir = "./" + _dir;
        String field = "text";
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexdir)));
        IndexSearcher searcher = new IndexSearcher(reader);
        PrintWriter writer = new PrintWriter(_dir + ".csv", "UTF-8");
        
        Searchit(reader,searcher, _analyzer, field, _searchList, _topn, writer);
        
        
        searcher.setSimilarity(new similarity_tf_rm());
        Searchit(reader,searcher, _analyzer, field, _searchList, _topn, writer);
        
        
        searcher.setSimilarity(new similiarty_queryNorm());
        Searchit(reader,searcher, _analyzer, field, _searchList, _topn, writer);

        writer.close();
        reader.close();
               /// searcher.setSimilarity(null);

    }

    public static void Searchit(IndexReader reader,IndexSearcher searcher, Analyzer _analyzer, String field, String[] _searchList, int _topn, PrintWriter writer) throws org.apache.lucene.queryparser.classic.ParseException, IOException, InvalidTokenOffsetsException {
        Analyzer analyzer = _analyzer;

        QueryParser parser = new QueryParser(Version.LUCENE_47, field, analyzer);

        String[] testString = _searchList;//{"to","123","impressed","Geezer","geezer","semi-busy","\"eggs vegetable\"","gs veget","\"gs veget\""};//,"good","I","but","coffee"};

        for (int j = 0; j < testString.length; j++) {
            String lstr = String.valueOf(j) + "," + testString[j];
            Query query = parser.parse(testString[j]);
            System.out.println("Searching for: " + query.toString(field));
            TopDocs topdocs = searcher.search(query, _topn);
            lstr += "," + topdocs.totalHits;
            ScoreDoc[] scoreDocs = topdocs.scoreDocs;
 SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
  Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query.rewrite(reader)));
            for (int i = 0; i < scoreDocs.length; i++) {
                int doc = scoreDocs[i].doc;
                Document document = searcher.doc(doc);
          //      System.out.println("Snippet=" + document.get(field));
 System.out.println(i);
		 String text = document.get(field);
    TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), doc, field, analyzer);
    TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 10);//highlighter.getBestFragments(tokenStream, text, 3, "...");
     String line="";
    for (int m = 0; m < frag.length; m++) {
      
      if ((frag[m] != null) && (frag[m].getScore() > 0)) {
        System.out.println((frag[m].toString()));    
    line=frag[m].toString();
   line=line.replaceAll("\n","");
   line=line.replaceAll("\r","");
   line=line.replaceAll("\"","");
   line=line.replaceAll(","," ");
   
      }
      
    }
     lstr += "," + line;
                lstr += "," + String.valueOf(scoreDocs[i].score);

            }
            writer.write(lstr + "\n");
            System.out.println("Search for:" + testString[j] + " Total hits=" + scoreDocs.length);
            System.out.println("////////////////////////////////////////////////////");
        }

    }
}
