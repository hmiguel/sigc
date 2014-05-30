package lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.codehaus.jettison.json.JSONObject;


public class Runner {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub

		Lucene luc = new Lucene();
		
		//System.out.println(luc.suggestTermsFor("lond"));
		
		//luc.CreateAutoCompleteIndexFromDic();
		
		//System.out.println(SpellCheck.Suggest("hello")); // TO SLOW
	
		//RecSys r = new RecSys();
		
		//System.out.println();
		
		
		
		//Lucene lu = new Lucene();
		
		//lu.init();
		
		//lu.buildIndex();
		
		//System.out.println(lu.QuerySearch("lisbon", 0));
	
		//System.out.println(j);
			
		//luc.buildIndex();
		
		//System.out.println(luc.QuerySearch("new york", 0));
	}

}
