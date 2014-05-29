package lucene;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter.Side;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.suggest.FileDictionary;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;



//import pt.uc.dei.ia.jena.TripleStoreReader;
import models.Article;
import models.NER.Person;

public final class AutoCompleter {

    private static final String GRAMMED_WORDS_FIELD = "words";

    private static final String SOURCE_WORD_FIELD = "sourceWord";

    private static final String COUNT_FIELD = "count";

    private static final String[] ENGLISH_STOP_WORDS = {
    "a", "an", "and", "are", "as", "at", "be", "but", "by",
    "for", "i", "if", "in", "into", "is",
    "no", "not", "of", "on", "or", "s", "such",
    "t", "that", "the", "their", "then", "there", "these",
    "they", "this", "to", "was", "will", "with"
    };

    private final FSDirectory autoCompleteDirectory;

    private IndexReader autoCompleteReader;
    
    private final File index;
    
    Analyzer analyzer = new Analyzer() {
    	  @Override
    	   protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
    	     Tokenizer source = new StandardTokenizer(null, reader);
    	     TokenStream filter = new StandardFilter(null, source);
    	    
    	     filter = new LowerCaseFilter(null, filter);
    	     filter = new StopFilter(null, filter, null);
    	     
    	     return new TokenStreamComponents(source, filter);
    	   }
    	 };


    private IndexSearcher autoCompleteSearcher;
    
    
    public JSONObject getPeople(List<Article> articles) throws JSONException{
    	
    	JSONObject js = new JSONObject();
    	
    	js.put("people", new JSONArray());
    	
    	for(Article a: articles){
    		
    		js = extractArticlePeople(a, js);
    		
    	}
    	
    	return js;
    }
  
    
    public JSONObject extractArticlePeople (Article a, JSONObject js){
    		  	
    	// Parse Person
    	List<Person> p = a.getPersons();
    	
    	JSONArray array = null;
    	
		try {
			array = js.getJSONArray("people");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
    	
    	for(Person person: p){
    		array.put(person.getPerson());
    	}
    	try {
			js.put("people", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}	
    	
    	return js;
    }
    
    
    
    public AutoCompleter(String autoCompleteDir) throws IOException {
    	
    	this.index = new File(autoCompleteDir);
    			
    	this.autoCompleteDirectory = FSDirectory.open(index);
     	
    	reOpenReader();
    }

    
    /* SUGESTED TERMS FUNCTION */
    public List<String> suggestTermsFor(String term) throws IOException {
    	
    	// get the top 5 terms for query
    	Query query = new TermQuery(new Term(GRAMMED_WORDS_FIELD, term));
    		
    	Sort sort = new Sort();
    
    	TopDocs docs = autoCompleteSearcher.search(query, null, 5, sort);
    	
    	List<String> suggestions = new ArrayList<String>();
    	
    	for (ScoreDoc doc : docs.scoreDocs) {
    		suggestions.add(autoCompleteReader.document(doc.doc).get(SOURCE_WORD_FIELD));
    	}

    	return suggestions;
    }
    
    /*
    @SuppressWarnings({ "unchecked", "deprecation" })
    public void reIndex(Directory sourceDirectory, String fieldToAutocomplete)
    		throws CorruptIndexException, IOException {
    	
    	// build a dictionary (from the spell package)
    	IndexReader sourceReader = IndexReader.open(sourceDirectory);

    	LuceneDictionary dict = new LuceneDictionary(sourceReader,
    			fieldToAutocomplete);

    	// use a custom analyzer so we can do EdgeNGramFiltering
    	
    	IndexWriterConfig config = new IndexWriterConfig(null, analyzer);
    	
    	IndexWriter writer = new IndexWriter(autoCompleteDirectory, config);
    	
    	// go through every word, storing the original word (incl. n-grams) 
    	// and the number of times it occurs
    	Map<String, Integer> wordsMap = new HashMap<String, Integer>();

    	Iterator<String> iter = (Iterator<String>) dict.getWordsIterator();
    	
    	while (iter.hasNext()) {
    		String word = iter.next();

    		int len = word.length();
    		if (len < 3) {
    			continue; // too short we bail but "too long" is fine...
    		}

    		if (wordsMap.containsKey(word)) {
    			throw new IllegalStateException(
    					"This should never happen in Lucene 2.3.2");
    			// wordsMap.put(word, wordsMap.get(word) + 1);
    		} else {
    			// use the number of documents this word appears in
    			wordsMap.put(word, sourceReader.docFreq(new Term(
    					fieldToAutocomplete, word)));
    		}
    	}

    	for (String word : wordsMap.keySet()) {
    		// ok index the word
    		Document doc = new Document();
    		
    		doc.add(new Field(SOURCE_WORD_FIELD, word, Field.Store.YES, Field.Index.NOT_ANALYZED)); // original
    		
    		doc.add(new Field(GRAMMED_WORDS_FIELD, word, Field.Store.YES, Field.Index.ANALYZED)); // grammed
    			
    		doc.add(new Field(COUNT_FIELD,
    				Integer.toString(wordsMap.get(word)), Field.Store.NO,
    				Field.Index.ANALYZED_NO_NORMS)); // count

    		writer.addDocument(doc);
    	}

    	sourceReader.close();

    	// close writer
    	writer.close();

    	// re-open our reader
    	reOpenReader();
    }*/

    @SuppressWarnings("deprecation")
	private void reOpenReader() throws CorruptIndexException, IOException {
    	
    	if (autoCompleteReader == null) {
    		
    		autoCompleteReader = IndexReader.open(autoCompleteDirectory);
    		
    	} else {
    		
    		autoCompleteReader.open(autoCompleteDirectory);
    		
    	}

    	autoCompleteSearcher = new IndexSearcher(autoCompleteReader);
    }

    public static void main(String[] args) throws Exception {
    	
    	// autocomplete index
    	AutoCompleter autocomplete = new AutoCompleter("index/autocomplete");

    	// Term to search
    	String term = "steve";

    	//Output
    	System.out.println(autocomplete.suggestTermsFor(term));
    	// prints [steve, steven, stevens, stevenson, stevenage]
    }

}