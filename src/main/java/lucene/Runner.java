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
		
		luc.buildIndex();
		
		//System.out.println(luc.suggestTermsFor("lond"));
		
		//luc.CreateAutoCompleteIndexFromDic();
		
		//System.out.println(SpellCheck.Suggest("hello")); // TO SLOW
	
		//RecSys r = new RecSys();
		
		//System.out.println();
		
		
		
		//Lucene lu = new Lucene();
		
		//lu.init();
		
		//lu.buildIndex();
		
		//System.out.println(luc.QuerySearch("obama", 0));
		/*
		
		List<Article> articles = DataReader.getArticles(); 
		
		// #ARTICLES
		System.out.println("SIZE: " + articles.size());

		// FOR EACH ARTICLE -> CREATE INDEX DOCUMENT
		for (Article article : articles) {
			System.out.println(article.getPersons());
		}
		
		*/
		//System.out.println(j);
			
		//luc.buildIndex();
		
		//System.out.println(luc.QuerySearch("new york", 0));
	}

}
