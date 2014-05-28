package lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import data.ArticlesReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



//import pt.uc.dei.ia.jena.TripleStoreReader;
import lucene.IndexBuilder;
import lucene.WordReader;
import models.Article;
import models.Word;

public class Lucene {

	private static StandardAnalyzer analyzer;
	private static File index;
	private static File triples;

	public boolean CreateAutoCompleteIndexFromDic() {

		File index = new File("C:/Users/hmiguel/workspace/index/autocomplete"); // index
																			// path

		if (index.isDirectory()) {

			if (index.list().length > 0) {
				// Index Directory must be empty!
				return false;
			}
		}

		try {
			// INDEX WRITER CONFIG
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40,
					analyzer);

			// INDEX WRITER CONFIG
			IndexWriter w = new IndexWriter(FSDirectory.open(index), config);

			// WORD DICIONARY READER
			List<Word> words = WordReader.GetWords();

			for (Word word : words) {

				IndexBuilder.addDocWords(w, word);
			}

			w.close(); // Close Writer

		} catch (Exception e) {

			return false;
		}

		return true;
	}

	public static boolean CreateIndexFromData(File index) {

		if (index.isDirectory()) {

			if (index.list().length > 0) {
				System.out.println("Index Directory must be empty!");
				return false;
			}
		}

		try {
			// INDEX WRITER CONFIG
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40,
					analyzer);

			// INDEX WRITER CONFIG
			IndexWriter w = new IndexWriter(FSDirectory.open(index), config);

			// ARTICLES READER - SIGC READER
			//List<Article> articles = TripleStoreReader.getArticles();
			List<Article> articles = ArticlesReader.getArticles(); 
			
			// #ARTICLES
			System.out.println("SIZE: " + articles.size());

			// FOR EACH ARTICLE -> CREATE INDEX DOCUMENT
			for (Article article : articles) {
				IndexBuilder.addDoc(w, article);
			}

			w.close(); // Close Writer

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/* SUGESTED TERMS FUNCTION */
	public JSONArray suggestTermsFor(String term) throws IOException {

		File auto_index;
		
		if (System.getProperty("os.name").contains("Windows")){
			auto_index = new File("C:/Users/hmiguel/workspace/index/autocomplete");
			
		}else{
			
			
			auto_index = new File("padsilva/index/autocomplete"); //TODO
		}
		

		IndexReader autoCompleteReader = DirectoryReader.open(FSDirectory
				.open(auto_index)); // READ
		IndexSearcher searcher = new IndexSearcher(autoCompleteReader);

		// get the top 5 terms for query
		// Query query = new TermQuery(new Term("word", term));

		Term t = new Term("word", term);

		Sort sort = new Sort();

		Query query = new PrefixQuery(t);
		TopDocs docs = searcher.search(query, 5, sort);

		// Query q = new PrefixQuery(new Term(term));

		//

		// TopDocs docs = searcher.search(q, null, 5, sort);

		// JSONOBJECT RESULTS

		JSONArray array = new JSONArray();

		// List<String> suggestions = new ArrayList<String>();

		for (ScoreDoc doc : docs.scoreDocs) {

			String wd = autoCompleteReader.document(doc.doc).get("word");

			// suggestions.add(w);

			array.put(wd);

		}

		return array;

	}

	public JSONObject getArticle(String res) {

		JSONObject results = new JSONObject();
		
		// Create <Article> JsonArray
		JSONArray data = new JSONArray();

		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(index)); // READ
			IndexSearcher searcher = new IndexSearcher(reader);
			TermQuery query = new TermQuery(new Term("resource", res));
			TopDocs topdocs = searcher.search(query, 1);

			ScoreDoc[] hits = topdocs.scoreDocs;

			for (int i = 0; i < hits.length; i++) {

				int docId = hits[i].doc;

				Document d = searcher.doc(docId);

				JSONObject article = new JSONObject();

				article.put("id", docId); // article Id
				article.put("label", d.get("label")); // label
				article.put("abstract", d.get("abstract")); // abstract
				article.put("resource", d.get("resource")); // resource
				article.put("description", d.get("description")); // description
				article.put("pubdate", d.get("date")); // pub date
				System.out.println(">>>" + d.get("description"));
				// PRINT ALL FIELDS OF DOC
				// d.getFields()

				// TOPICS
				JSONArray topics = new JSONArray();
				IndexableField[] index_top = d.getFields("topic");
				for (int j = 0; j < index_top.length; j++) {
					topics.put(index_top[j].stringValue());
				}
				article.put("topics", topics);

				// CATEGORIES
				JSONArray cat = new JSONArray();
				IndexableField[] index_cat = d.getFields("category");
				for (int j = 0; j < index_cat.length; j++) {
					cat.put(index_cat[j].stringValue());
				}
				article.put("categories", cat);

				int index_d = data.length();

				data.put(index_d, article); // Add Article to JsonArray

			}

			results.put("data", data); // Add JsonArray to Json Result

		} catch (Exception e) {

			return new JSONObject();
		}
		
		return results;
	}

	public JSONObject QuerySearch(String querystr, int page) {

		JSONObject results = new JSONObject();

		try {

			// the "title" arg specifies the default field to use // when no
			// field is explicitly specified in the query.				
			
			MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_40,
                    new String[] {"label", "abstract"},
                    analyzer);

			// SEARCH
			int hitsPerPage = 10;
			int pageOffset = 10;

			IndexReader reader = DirectoryReader.open(FSDirectory.open(index)); // READ
			IndexSearcher searcher = new IndexSearcher(reader);

			// COLLECTOR

			TopScoreDocCollector collector = TopScoreDocCollector.create(500,
					true);

			searcher.search(queryParser.parse(querystr), collector);

			// SCORE DOCS
			// TopDocs hits = collector.topDocs(maxReturn*page);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			// BUILD RESPONSE
			results.put("query", querystr); // Add Query to Json Result
			results.put("hits", hits.length); // Add Hits to Json Result

			// MEANING QUERY 1-WORD
			if (querystr.split(" ").length < 2) {
				results.put("mean", SpellCheck.Suggest(querystr)); // Add Mean
																	// to Json
																	// Result
			} else {
				results.put("mean", "");
			}

			// Create <Article> JsonArray
			JSONArray data = new JSONArray();

			// for each hit
			for (int i = pageOffset * page; i < Math.min(hitsPerPage
					* (page + 1), hits.length); i++) {

				int docId = hits[i].doc;

				Document d = searcher.doc(docId);

				JSONObject article = new JSONObject();

				article.put("id", docId); // article Id
				article.put("label", d.get("label")); // label
				article.put("abstract", d.get("abstract")); // abstract
				article.put("resource", d.get("resource")); // resource
				article.put("description", d.get("description")); // description
				article.put("date", d.get("date")); // pub date
				// PRINT ALL FIELDS OF DOC
				// d.getFields()
				
				// PEOPLE
				JSONArray peo = new JSONArray();
				IndexableField[] index_peo = d.getFields("person");
				for (int j = 0; j < index_peo.length; j++) {
					peo.put(index_peo[j].stringValue());
				}
				article.put("people", peo);
				
				// ORGANIZATIONS
				JSONArray org = new JSONArray();
				IndexableField[] index_org = d.getFields("organization");
				for (int j = 0; j < index_org.length; j++) {
					org.put(index_org[j].stringValue());
				}
				article.put("organizations", org);
				
				// LOCALS
				JSONArray loc = new JSONArray();
				IndexableField[] index_loc = d.getFields("local");
				for (int j = 0; j < index_loc.length; j++) {
					loc.put(index_loc[j].stringValue());
				}
				article.put("locals", loc);

				// TOPICS
				JSONArray topics = new JSONArray();
				IndexableField[] index_top = d.getFields("topic");
				for (int j = 0; j < index_top.length; j++) {
					topics.put(index_top[j].stringValue());
				}
				article.put("topics", topics);

				// CATEGORIES
				JSONArray cat = new JSONArray();
				IndexableField[] index_cat = d.getFields("category");
				for (int j = 0; j < index_cat.length; j++) {
					cat.put(index_cat[j].stringValue());
				}
				article.put("categories", cat);

				int index_d = data.length();

				data.put(index_d, article); // Add Article to JsonArray

			}

			results.put("data", data); // Add JsonArray to Json Result

			reader.close();

		} catch (Exception e) {

			return new JSONObject();

		}

		return results;

	}

	public void init() {

		// System.out.println("Lucene Starting...");

		analyzer = new StandardAnalyzer(Version.LUCENE_40);
		
		if (System.getProperty("os.name").contains("Windows")){
			index = new File("C:/Users/hmiguel/workspace/index/");
		}else{
			
			index = new File("/home/padsilva/Desktop/SIGC/index/"); //TODO
		}
		
	}

	public static void buildIndex() {

		System.out.print("Creating Index...");

		if (!CreateIndexFromData(index)) { // FIRST TIME
			System.out.println("ERROR>>");
			return;
		} else {
			System.out.println("OK");
		}

	}

	public Lucene() {

		init();

	}

}