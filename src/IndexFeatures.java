import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class IndexFeatures {
  public static void main(String[] args) throws IOException, ParseException {
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

    // 1. create the index
    File f1=new File("/media/LENOVO/ind/indexFiles");
 
    //Directory index = new RAMDirectory();
    //FSDirectory.getDisableLocks();
    IndexWriter w = new IndexWriter(FSDirectory.open(f1), analyzer, true,
        IndexWriter.MaxFieldLength.UNLIMITED);
    String inp="/media/LENOVO/Dataset/input/all";
    File f=new File(inp);
	File[] li=f.listFiles();
	for(int p=0;p<li.length;p++)
	{
		String lin="";
		BufferedReader br = new BufferedReader(new FileReader(li[p]));
		while((lin=br.readLine())!=null){
			addDoc(w, lin+"		file: "+li[p].getName());
		}
	}
    w.optimize();
    w.close();

    // 2. query
    /*String querystr = args.length > 0 ? args[0] : "adam florence";
    Query q = new QueryParser("title", analyzer).parse(querystr);

    // 3. search
    int hitsPerPage = 10;
    IndexSearcher searcher = new IndexSearcher(FSDirectory.getDirectory(f1), true);
    TopScoreDocCollector collector = 
    TopScoreDocCollector.create(hitsPerPage, true);
    searcher.search(q, collector);
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
    searcher.close();*/
  }

  private static void addDoc(IndexWriter w, String value) throws IOException {
    Document doc = new Document();
    doc.add(new Field("title", value, Field.Store.YES, Field.Index.ANALYZED));
    w.addDocument(doc);
  }
}