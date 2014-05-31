package lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import models.Article;

import org.apache.lucene.queryparser.classic.ParseException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import data.DataReader;


public class Runner {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws IOException, ParseException, JSONException {
		// TODO Auto-generated method stub

		Lucene luc = new Lucene();
		
		//luc.buildIndex();
		
		System.out.println(luc.suggestTermsFor("lond"));
		
		//luc.CreateAutoCompleteIndexFromDic();
		
		//lu.buildIndex();

	}

}
