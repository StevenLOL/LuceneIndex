/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ci6226;

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
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author steven
 */
public class buildindex {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
        String file = "/home/steven/Dropbox/workspace/ntu_coursework/ci6226/Assiment/yelpdata/yelp_training_set/yelp_training_set_review.json";
        JSONParser parser = new JSONParser();

        BufferedReader in = new BufferedReader(new FileReader(file));
      //  List<Document> jdocs = new LinkedList<Document>();
        Date start = new Date();
        String indexPath = "./myindex";
        System.out.println("Indexing to directory '" + indexPath + "'...");
        // Analyzer analyzer= new NGramAnalyzer(2,8);
        Analyzer analyzer= new myAnalyzer();
       
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
                Directory dir = FSDirectory.open(new File(indexPath));
        // :Post-Release-Update-Version.LUCENE_XY:
        // TODO: try different analyzer,stop words,words steming check size
     //   Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
       
        
        // Add new documents to an existing index:
       // iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
         iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
      // Optional: for better indexing performance, if you
        // are indexing many documents, increase the RAM
        // buffer.  But if you do this, increase the max heap
        // size to the JVM (eg add -Xmx512m or -Xmx1g):
        //
        // iwc.setRAMBufferSizeMB(256.0);
        IndexWriter writer = new IndexWriter(dir, iwc);
      //  writer.addDocuments(jdocs);
        int line=0;
        while (in.ready()) {
            String s = in.readLine();
            Object obj = JSONValue.parse(s);
            JSONObject person = (JSONObject) obj;
            String text = (String) person.get("text");
            String user_id = (String) person.get("user_id");
            String business_id = (String) person.get("business_id");
            String review_id = (String) person.get("review_id");
            JSONObject votes = (JSONObject) person.get("votes");
            long funny = (Long) votes.get("funny");
            long cool = (Long) votes.get("cool");
            long useful = (Long) votes.get("useful");
            Document doc = new Document();
            Field review_idf = new StringField("review_id", review_id, Field.Store.YES);
            doc.add(review_idf);
            Field business_idf = new StringField("business_id", business_id, Field.Store.YES);
            doc.add(business_idf);
           
            //http://qindongliang1922.iteye.com/blog/2030639
            FieldType ft=new FieldType();  
        ft.setIndexed(true);//存储  
        ft.setStored(true);//索引  
        ft.setStoreTermVectors(true);  
        ft.setTokenized(true);  
        ft.setStoreTermVectorPositions(true);//存储位置  
        ft.setStoreTermVectorOffsets(true);//存储偏移量  
    
            Field textf = new Field("text", text, ft );
           
     
            doc.add(textf);
        //    Field user_idf = new StringField("user_id", user_id, Field.Store.YES);
       //     doc.add(user_idf);
      //      doc.add(new LongField("cool", cool, Field.Store.YES));
      //      doc.add(new LongField("funny", funny, Field.Store.YES));
     //       doc.add(new LongField("useful", useful, Field.Store.YES));
            
            writer.addDocument(doc);

            System.out.println(line++);
        }

        writer.close();
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + " total milliseconds");
// BufferedReader in = new BufferedReader(new FileReader(file));
//while (in.ready()) {
//  String s = in.readLine();
//  //System.out.println(s);
        // JSONObject jsonObject = (JSONObject) ((Object)s);
//		String rtext = (String) jsonObject.get("text");
//		System.out.println(rtext);
//		//long age = (Long) jsonObject.get("age");
//		//System.out.println(age);
//}
//in.close();
    }

    /**
     * Indexes the given file using the given writer, or if a directory is
     * given, recurses over files and directories found under the given
     * directory.
     *
     * NOTE: This method indexes one document per input file. This is slow. For
     * good throughput, put multiple documents into your input file(s). An
     * example of this is in the benchmark module, which can create "line doc"
     * files, one document per line, using the
     * <a
     * href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
     * >WriteLineDocTask</a>.
     *
     * @param writer Writer to the index where the given file/dir info will be
     * stored
     * @param file The file to index, or the directory to recurse into to find
     * files to index
     * @throws IOException If there is a low-level I/O error
     */
    static void indexDocs(IndexWriter writer, String s) throws IOException {
        Object obj = JSONValue.parse(s);
        JSONObject person = (JSONObject) obj;

        String text = (String) person.get("text");
        //  System.out.println(text);

        String user_id = (String) person.get("user_id");
        //  System.out.println(user_id);

        String business_id = (String) person.get("business_id");
        //  System.out.println(business_id);
        String review_id = (String) person.get("review_id");
        //   System.out.println(review_id);

        JSONObject votes = (JSONObject) person.get("votes");

        long funny = (Long) votes.get("funny");
        // System.out.println(funny);

        long cool = (Long) votes.get("cool");
        //   System.out.println(cool);

        long useful = (Long) votes.get("useful");
   // System.out.println(useful);
        // do not try to index files that cannot be read

        // make a new, empty document
        Document doc = new Document();

        // Add the path of the file as a field named "path".  Use a
        // field that is indexed (i.e. searchable), but don't tokenize 
        // the field into separate words and don't index term frequency
        // or positional information:
        Field review_idf = new StringField("review_id", review_id, Field.Store.YES);
        doc.add(review_idf);
        Field business_idf = new StringField("business_id", business_id, Field.Store.YES);
        doc.add(business_idf);
        Field textf = new TextField("text", text, Field.Store.YES);
        doc.add(textf);
        Field user_idf = new StringField("user_id", user_id, Field.Store.YES);
        doc.add(user_idf);

        // Add the last modified date of the file a field named "modified".
        // Use a LongField that is indexed (i.e. efficiently filterable with
        // NumericRangeFilter).  This indexes to milli-second resolution, which
        // is often too fine.  You could instead create a number based on
        // year/month/day/hour/minutes/seconds, down the resolution you require.
        // For example the long value 2011021714 would mean
        // February 17, 2011, 2-3 PM.
        doc.add(new LongField("cool", cool, Field.Store.YES));
        doc.add(new LongField("funny", funny, Field.Store.YES));
        doc.add(new LongField("useful", useful, Field.Store.YES));

        // Add the contents of the file to a field named "contents".  Specify a Reader,
        // so that the text of the file is tokenized and indexed, but not stored.
        // Note that FileReader expects the file to be in UTF-8 encoding.
        // If that's not the case searching for special characters will fail.
        //  if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
        // New index, so we just add the document (no old document can be there):
        //   System.out.println("adding " + review_id);
        writer.addDocument(doc);
        //       } else {
        // Existing index (an old copy of this document may have been indexed) so 
        // we use updateDocument instead to replace the old one matching the exact 
        // path, if present:
        //         System.out.println("updating " + file);
        ///          writer.updateDocument(new Term("review_id", review_id), doc);
        //     }

    }

}


/*
 {"votes": {"funny": 0, "useful": 0, "cool": 0}, "user_id": "t5qFxYP2t6ltRbvhqJsZAQ", "review_id": "k6fuC6MOeYkh6QdMqXDD_Q", "stars": 5, "date": "2010-11-28", "text": "REAL NY STYLE CHINESE FOOD HERE IN ARIZONA!!! \nFREE HOT TEA AND FRIED WON-TON SKINS LIKE BACK HOME! (BY REQUEST)\n\nI am from New Jersey and so are the owners.  They opened up shop here 1 year after I moved here (1998) and I was delighted i could get a real NY egg-roll and spare ribs in Arizona.  Their best dish i feel is the Szechuan Garlic Chicken (or shrimp).  Try it once and you may never try anything else on the menu it's that good!!  Taste's the same as the best of NYC.\n\nTheir lunch specials are amazing.  Comes with soup, spring roll, crab puff, fried rice, hot tea (by request) all for a very low price.  Sunday is Dim Sum if you like dumplings.  This place NEVER disappoints and I bring work clients here from New York all the time.\n\nthey all agree, REAL NY CHINESE FOOD!!!!", "type": "review", "business_id": "zxqvU415r_RtZRKDtdbIKQ"}

 {"votes": {"funny": 0, "useful": 0, "cool": 0}, 
 "user_id": "t5qFxYP2t6ltRbvhqJsZAQ", 
 "review_id": "k6fuC6MOeYkh6QdMqXDD_Q", 
 "stars": 5, 
 "date": "2010-11-28", 
 "text": "REAL NY STYLE CHINESE FOOD HERE IN ARIZONA!!! \nFREE HOT TEA AND FRIED WON-TON SKINS LIKE BACK HOME! (BY REQUEST)\n\nI am from New Jersey and so are the owners.  They opened up shop here 1 year after I moved here (1998) and I was delighted i could get a real NY egg-roll and spare ribs in Arizona.  Their best dish i feel is the Szechuan Garlic Chicken (or shrimp).  Try it once and you may never try anything else on the menu it's that good!!  Taste's the same as the best of NYC.\n\nTheir lunch specials are amazing.  Comes with soup, spring roll, crab puff, fried rice, hot tea (by request) all for a very low price.  Sunday is Dim Sum if you like dumplings.  This place NEVER disappoints and I bring work clients here from New York all the time.\n\nthey all agree, REAL NY CHINESE FOOD!!!!", 
 "type": "review", 
 "business_id": "zxqvU415r_RtZRKDtdbIKQ"}

 */
