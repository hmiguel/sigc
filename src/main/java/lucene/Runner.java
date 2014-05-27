package lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import lucene.WordReader;
import models.Word;

public class Runner {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		//Lucene luc = new Lucene();
		
		//luc.CreateAutoCompleteIndexFromDic();
		
		//System.out.println(SpellCheck.Suggest("hello")); // TO SLOW
	
		//Sparql sparql = new Sparql();
		
		//RecSys r = new RecSys();
		
		Lucene lu = new Lucene();
		
		lu.init();
		
		//lu.buildIndex();
		
		System.out.println(lu.QuerySearch("lisbon", 0));
	
		//System.out.println(j);
			
		//luc.buildIndex();
		
		//System.out.println(luc.QuerySearch("new york", 0));
	}

}
