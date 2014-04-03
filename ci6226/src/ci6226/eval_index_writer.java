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


public class eval_index_writer {
    public eval_index_writer(Analyzer _analyzer,String _iReviewLocation,String _dir) throws IOException{
         String file = _iReviewLocation;
        JSONParser parser = new JSONParser();
        BufferedReader in = new BufferedReader(new FileReader(file));    
        Date start = new Date();
        String indexPath = "./"+_dir;
        System.out.println("Indexing to directory '" + indexPath + "'...");
        Analyzer analyzer= _analyzer;       
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        Directory dir = FSDirectory.open(new File(indexPath));  
        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);  
        IndexWriter writer = new IndexWriter(dir, iwc);    
      //  int line=0;
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
        //    Field business_idf = new StringField("business_id", business_id, Field.Store.YES);
       //     doc.add(business_idf);
           
            //http://qindongliang1922.iteye.com/blog/2030639
            FieldType ft=new FieldType();  
            ft.setIndexed(true);//
            ft.setStored(true);//
            ft.setStoreTermVectors(true);  
            ft.setTokenized(true);  
            ft.setStoreTermVectorPositions(true);//
            ft.setStoreTermVectorOffsets(true);//
    
            Field textf = new Field("text", text, ft );
           
     
            doc.add(textf);
        //    Field user_idf = new StringField("user_id", user_id, Field.Store.YES);
       //     doc.add(user_idf);
      //      doc.add(new LongField("cool", cool, Field.Store.YES));
      //      doc.add(new LongField("funny", funny, Field.Store.YES));
     //       doc.add(new LongField("useful", useful, Field.Store.YES));
            
            writer.addDocument(doc);

          //  System.out.println(line++);
        }

        writer.close();
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + " total milliseconds");
    }
}
