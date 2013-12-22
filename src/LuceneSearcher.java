

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneSearcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length != 2){
			System.out.println("INVALID ARGUMENTS");
			System.exit(0);
		}
		
		long time = System.currentTimeMillis();
		
		String qryStr = args[1].trim();
		qryStr=qryStr.replaceAll("_", " ");
		
		try{
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
			Query qry = new QueryParser("title", analyzer).parse(qryStr);
			File f1=new File("/media/LENOVO/ind/indexFiles");
			int hitsPerPage = 30;
		    IndexSearcher searcher = new IndexSearcher(FSDirectory.getDirectory(f1), true);
		    TopScoreDocCollector collector = 
		    TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(qry, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    
		    // 4. display results
		    System.out.println("Found " + hits.length + " hits.");
		    for(int i=0;i<hits.length;++i) {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      System.out.println((i + 1) + ". " + d.get("title"));
		    }

		    // searcher can only be closed when there
		    // is no need to access the documents any more. 
		    searcher.close();
			
			/*Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
			Query qry = new QueryParser("title", analyzer).parse(qryStr);
			
			//Directory index = FSDirectory.getDirectory("/media/LENOVO/ind/indexFiles/");
			//System.out.println(index.fileExists("/media/LENOVO/ind/indexFiles/"));
			int hitsPerPage = 10;
			File f1=new File("/media/LENOVO/ind/indexFiles");
			IndexSearcher searcher = new IndexSearcher(FSDirectory.getDirectory(f1), true);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(qry, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			System.out.println("Found " + hits.length + " hits.");
			for(int i=0;i<hits.length;++i) {
			    int docId = hits[i].doc;
			    Document d = searcher.doc(docId);
			    System.out.println((i + 1) + ". " + d.get("path"));
			}*/
		
		}catch(Exception e){	e.printStackTrace();	}

		System.out.println("Total time = "+ (System.currentTimeMillis()-time)+ "milliseconds");

	}

}
