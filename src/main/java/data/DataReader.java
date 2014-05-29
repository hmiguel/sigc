package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.*;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;













import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import stanford.StringtoNER;
import models.Article;
import models.Category;
import models.Entity;
import models.Topic;

public class DataReader {
	
	public static List<Article> getArticles() throws JSONException, IOException{
		
		List<Article> list = new ArrayList<Article>();
		
		
		// READ JSON DATABASE <- INFO
		String info_path = "C:\\Users\\hmiguel\\Desktop\\Faculdade\\2014\\2S\\SIGC-Proj\\data\\info.json";
	
		FileInputStream jsonFile = null;
		JSONObject json = null;
		try {
			jsonFile = new FileInputStream(new File(info_path));
			String jsonStr = IOUtils.toString(jsonFile, "UTF-8");
			json = new JSONObject(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		// READ DIRECTORIES
		String base = "C:\\Users\\hmiguel\\Desktop\\Faculdade\\2014\\2S\\SIGC-Proj\\data";
		File folder = new File(base);
		
		// LOAD Classifier
		String serializedClassifier = "classifiers/english.muc.7class.nodistsim.crf.ser.gz";      
        AbstractSequenceClassifier classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		
		
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				File inside_dir = new File(base+ "\\" + fileEntry.getName());
				for (final File fileEntry_dir : inside_dir.listFiles()) {
					
					Article a = new Article();
					
					String id = fileEntry_dir.getName().substring(0, fileEntry_dir.getName().lastIndexOf('.'));
					
					JSONObject jart = json.getJSONObject(id);
					
					 //LABEL
					String label = (String) jart.get("title");
					a.setLabel(label);
					
					String file_path = base + "\\" + fileEntry.getName() + "\\" + fileEntry_dir.getName();
					
					//CONTENT
					FileInputStream articleFile = new FileInputStream(new File(base + "\\" + fileEntry.getName() + "\\" + fileEntry_dir.getName() ));
					String content = IOUtils.toString(articleFile, "UTF-8");
					a.setAbstract(content);
					
					//CATEGORY
					String catName = fileEntry.getName();
					Category category = new Category();
					category.setCategory(catName);
					List<Category> catlist = new ArrayList<Category>();
					catlist.add(category);
					a.setCategories(catlist);
					
					//RESOURCE
					a.setResource(jart.get("link").toString());
				
					//Description
					a.setDesc(jart.get("description").toString());
					
					//PublishDate
					a.setPublishDate(jart.getString("pubdate").toString());
					
					//NER DATA
					a = StringtoNER.generateNER(a, classifier); 
					
					list.add(a);
				}
			}
	   
	}
		return list;
	}
	
	public static List<Entity> getEntities() throws JSONException, IOException{
		
		List<Entity> list = new ArrayList<Entity>();
		
		
		// READ JSON DATABASE <- INFO
		String info_path = "C:\\Users\\hmiguel\\Desktop\\Faculdade\\2014\\2S\\SIGC-Proj\\ner.json";
	
		FileInputStream jsonFile = null;
		JSONObject json = null;
		try {
			jsonFile = new FileInputStream(new File(info_path));
			String jsonStr = IOUtils.toString(jsonFile, "UTF-8");
			json = new JSONObject(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return null;
		}
		
		//Entity e = new Entity();
		//['money', 'percent', 'person', 'time', 'date', 'org', 'locals']
		
		//PEOPLE
		JSONArray people = json.getJSONArray("person");
		
	    for(int i = 0; i< people.length(); i++){
	    	Entity e = new Entity();
	    	e.setType("People");
	    	e.setEntity((String) people.get(i));	
	    	e.setOcurrence(randInt(1,5));
	    	list.add(e);
	    }
	    
	  //LOCALS
	  	JSONArray locals = json.getJSONArray("locals");
	  		
	  	for(int i = 0; i< locals.length(); i++){
	  	   	Entity e = new Entity();
	  	   	e.setType("Locals");
	  	   	e.setEntity((String) locals.get(i));	
	  	   	e.setOcurrence(randInt(1,5));
	  	   	list.add(e);
	  	}
	  	
	  	 //ORGs
	  	JSONArray orgs = json.getJSONArray("org");
	  		
	  	for(int i = 0; i< orgs.length(); i++){
	  	   	Entity e = new Entity();
	  	   	e.setType("Organizations");
	  	   	e.setEntity((String) orgs.get(i));	
	  	   	e.setOcurrence(randInt(1,5));
	  	   	list.add(e);
	  	}
	    
	
		return list;
	}
	
	public static int randInt(int min, int max) {

	    // Usually this should be a field rather than a method variable so
	    // that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	
}
