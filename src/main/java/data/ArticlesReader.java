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

import org.apache.commons.io.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;









import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import stanford.StringtoNER;
import models.Article;
import models.Category;
import models.Topic;

public class ArticlesReader {
	
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
	
}
