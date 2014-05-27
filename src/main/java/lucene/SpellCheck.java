package lucene;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SpellCheck {

	@SuppressWarnings("deprecation")
	public static String Suggest(String wordForSuggestions){

		try{
			File dir = new File("index/suggest"); // dict
	
			Directory directory = FSDirectory.open(dir);
	
			SpellChecker spellChecker = new SpellChecker(directory);
	
			spellChecker.indexDictionary(new PlainTextDictionary(new File(
					"/usr/share/dict/words")), new IndexWriterConfig(
					Version.LUCENE_CURRENT, new StandardAnalyzer(
							Version.LUCENE_CURRENT)), false);
	
			int suggestionsNumber = 1;
			
			
			//Verify if word it's on dicionary
			if(!spellChecker.exist(wordForSuggestions)){
	
				String[] suggestions = spellChecker.suggestSimilar(wordForSuggestions,
						suggestionsNumber);
		
				if (suggestions != null && suggestions.length > 0) {
					for (String word : suggestions) {
						
						//only one
						if (!word.toLowerCase().equals(wordForSuggestions.toLowerCase())){
							return word;
						}
							
					}
				} 
			}
			
			
			spellChecker.close();
		}catch(Exception e){
			
		}
		
		return "";
	}

}
