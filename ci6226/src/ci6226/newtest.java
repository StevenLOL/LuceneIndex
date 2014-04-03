package ci6226;
import ci6226.NGramAnalyzer;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONValue;

import org.apache.lucene.analysis.Analyzer;
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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.EnglishStemmer;


public class newtest {
 
	public static void main(String[] args) throws Exception {
		test();
	}
 
	public static void test() throws Exception {
		
            /*Directory dir = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,
				new StandardAnalyzer(Version.LUCENE_47));
		IndexWriter writer = new IndexWriter(dir, config);
 
		Document doc1 = new Document();
		Document doc2 = new Document();
		Document doc3 = new Document();
 
		doc1.add(new TextField("bookname", "bc bc", Store.YES));
 
		doc2.add(new TextField("bookname", "ab bc", Store.YES));
 
		doc3.add(new TextField("bookname", "ab bc cd", Store.YES));
 
		writer.addDocument(doc1);
		writer.addDocument(doc2);
		writer.addDocument(doc3);
 
		writer.close();
            */
                String indexdir = "./myindex";
                String field = "text";
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexdir)));
		IndexSearcher searcher = new IndexSearcher(reader);
 //               searcher.setSimilarity();
                 //Analyzer analyzer = new NGramAnalyzer(2,8);
                //Analyzer analyzer= new myAnalyzer();
              
               // Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_47); //semi-busy
                    Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_47);
                //Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
              //   Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_47);
              //  Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_47);
       //         LowerCaseFilter lcf= new LowerCaseFilter(,);
                
                
                 //http://stackoverflow.com/questions/5391840/stemming-english-words-with-lucene
                 
                //EnglishStemmer  
                QueryParser parser = new QueryParser(Version.LUCENE_47, field, analyzer);
            
            
         //   searcher.search(query, null, 100);
                 
             String[] testString={"to","123","impressed","Geezer","geezer","semi-busy","\"eggs vegetable\"","gs veget","\"gs veget\""};//,"good","I","but","coffee"};
             for( int j=0;j<testString.length;j++){
		//TermQuery query = new TermQuery(new Term(field, testString[j]));
                  Query query = parser.parse(testString[j]);
            System.out.println("Searching for: " + query.toString(field));
		TopDocs topdocs = searcher.search(query, 5);
		ScoreDoc[] scoreDocs = topdocs.scoreDocs;
 
		for (int i = 0; i < scoreDocs.length; i++) {
			int doc = scoreDocs[i].doc;
			Document document = searcher.doc(doc);
			System.out.println("text====" + document.get(field));
			System.out.println("l1="+searcher.explain(query, doc));//
			System.out.println("l2="+scoreDocs[i].score);
		}
                 System.out.println("Search for:"+ testString[j]+" Total hits="+scoreDocs.length);
                System.out.println("////////////////////////////////////////////////////");
             }
		reader.close();
	}
}


/*
 public TopDocs search(Query query, int n)
    throws IOException {
    return search(query, null, n);
  }

 public TopDocs search(Query query, Filter filter, int n)
    throws IOException {
    return search(createNormalizedWeight(wrapFilter(query, filter)), null, n);
  }

  public TopDocs search(Query query, Filter filter, int n)
    throws IOException {
    return search(createNormalizedWeight(wrapFilter(query, filter)), null, n);
  }

  /**
   * Creates a normalized weight for a top-level {@link Query}.
   * The query is rewritten by this method and {@link Query#createWeight} called,
   * afterwards the {@link Weight} is normalized. The returned {@code Weight}
   * can then directly be used to get a {@link Scorer}.
   * @lucene.internal
   */

/*
  public Weight createNormalizedWeight(Query query) throws IOException {
    query = rewrite(query);
    Weight weight = query.createWeight(this);
    float v = weight.getValueForNormalization();
    float norm = getSimilarity().queryNorm(v);
    if (Float.isInfinite(norm) || Float.isNaN(norm)) {
      norm = 1.0f;
    }
    weight.normalize(norm, 1.0f);
    return weight;
  }

*/
