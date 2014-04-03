/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ci6226;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author steven
 */
public class loadIndex {
    public static final String ANSI_RESET = "\u001B[0m";
public static final String ANSI_BLACK = "\u001B[30m";
public static final String ANSI_RED = "\u001B[31m";
public static final String ANSI_GREEN = "\u001B[32m";
public static final String ANSI_YELLOW = "\u001B[33m";
public static final String ANSI_BLUE = "\u001B[34m";
public static final String ANSI_PURPLE = "\u001B[35m";
public static final String ANSI_CYAN = "\u001B[36m";
public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) throws Exception {
        String usage
                = "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
        if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
            System.out.println(usage);
            System.exit(0);
        }
        String index = "./myindex";
        String field = "text";
        String queries = null;
        int hitsPerPage = 10;
        boolean raw = false;
        
        
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
            doPagingSearch(in, searcher, query, hitsPerPage, raw, true, analyzer);
        }

        reader.close();
    }

    /**
     * This demonstrates a typical paging search scenario, where the search
     * engine presents pages of size n to the user. The user can then go to the
     * next page if interested in the next hits.
     *
     * When the query is executed for the first time, then only enough results
     * are collected to fill 5 result pages. If the user wants to page beyond
     * this limit, then the query is executed another time and all hits are
     * collected.
     *
     */
    public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query,
            int hitsPerPage, boolean raw, boolean interactive, Analyzer analyzer) throws IOException, InvalidTokenOffsetsException {

        // Collect enough docs to show 5 pages
        TopDocs results = searcher.search(query, 5 * hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = results.totalHits;
        System.out.println(numTotalHits + " total matching documents");

        int start = 0;
        int end = Math.min(numTotalHits, hitsPerPage);

        while (true) {
            if (end > hits.length) {
                System.out.println("Only results 1 - " + hits.length + " of " + numTotalHits + " total matching documents collected.");
                System.out.println("Collect more (y/n) ?");
                String line = in.readLine();
                if (line.length() == 0 || line.charAt(0) == 'n') {
                    break;
                }

                hits = searcher.search(query, numTotalHits).scoreDocs;
            }

            end = Math.min(hits.length, start + hitsPerPage);

            for (int i = start; i < end; i++) {
                if (raw) {                              // output raw format
                    System.out.println("doc=" + hits[i].doc + " score=" + hits[i].score);
                    continue;
                }

                Document doc = searcher.doc(hits[i].doc);
                String path = doc.get("review_id");
                if (path != null) {
                    System.out.println(ANSI_BLUE+(i + 1) + ANSI_RESET+"\nScore=\t" + hits[i].score);
                    String title = doc.get("business_id");
                    if (title != null) {
                       
                        String text = doc.get("text");
                        TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), hits[i].doc, "text", doc, analyzer);//TokenSources.getAnyTokenStream(searcher.getIndexReader() ,"text", analyzer);
                        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter(ANSI_RED,ANSI_RESET);
                        // SimpleFragmenter fragmenter = new SimpleFragmenter(80);
                        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
                        TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 4);
                        System.out.print("Snippet=\t");
                        for (int j = 0; j < frag.length; j++) {
                            if ((frag[j] != null) && (frag[j].getScore() > 0)) {
                                System.out.println((frag[j].toString()));
                            }
                        }
                         //System.out.print("\n");
                         System.out.println("Full Review=\t" + doc.get("text")+"\nBusinessID=\t" + title);
                    }
                } else {
                    System.out.println((i + 1) + ". " + "No path for this document");
                }

            }

            if (!interactive || end == 0) {
                break;
            }

            if (numTotalHits >= end) {
                boolean quit = false;
                while (true) {

                    System.out.print("Press ");
                    if (start - hitsPerPage >= 0) {
                        System.out.print("(p)revious page, ");
                    }
                    if (start + hitsPerPage < numTotalHits) {
                        System.out.print("(n)ext page, ");
                    }
                    System.out.println("(q)uit or enter number to jump to a page.");
                    int cpage = start / hitsPerPage;
                    System.out.println(String.format("Current page=%d,max page=%d", cpage + 1, 1 + numTotalHits / hitsPerPage));
                    String line = in.readLine();
                    if (line.length() == 0 || line.charAt(0) == 'q') {
                        quit = true;
                        break;
                    }
                    if (line.charAt(0) == 'p') {
                        start = Math.max(0, start - hitsPerPage);
                        break;
                    } else if (line.charAt(0) == 'n') {
                        if (start + hitsPerPage < numTotalHits) {
                            start += hitsPerPage;
                        }
                        break;
                    } else {
                        int page = Integer.parseInt(line);
                        if ((page - 1) * hitsPerPage < numTotalHits) {
                            start = (page - 1) * hitsPerPage;
                            break;
                        } else {
                            System.out.println("No such page");
                        }
                    }
                }
                if (quit) {
                    break;
                }
                end = Math.min(numTotalHits, start + hitsPerPage);
            }
        }

    }

}


/*

Query qry= QueryParser.Parse(query,new StandardAnalyzer());
Highlighter highlighter = new Highlighter(new QueryScorer(qry));

Hits hits = searcher.Search(qry);

// Iterate through the results:
for (int i = 0; i < lHits; i++)
{
    Document hitDoc = hits.Doc(i);
    String desc = hitDoc.Get("Contents");
    TokenStream tokenStream = analyzer.TokenStream("Contents", new System.IO.StringReader(desc));

    highlights[i] = highlighter.GetBestFragment(tokenStream, desc);
}

*/
