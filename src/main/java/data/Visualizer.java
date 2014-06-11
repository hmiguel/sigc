package data;

import java.io.IOException;

import lucene.Lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.FSDirectory;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Visualizer {
	
	
	public static JSONArray getAllData() throws IOException, JSONException{
		
		JSONArray data = new JSONArray();
		
		Lucene lu = new Lucene();
		
		JSONArray nodes = new JSONArray();
		JSONArray links = new JSONArray();
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(lu.getindexpath())); // READ
					
	    JSONArray full_topics = new JSONArray();

	    
		for (int i=0; i<reader.maxDoc(); i++){			   

			    Document d = reader.document(i);
			    //String docId = d.get("docId");
			    
			    JSONObject visual = new JSONObject();
			    
			    // TOPICS
				JSONArray topics = new JSONArray();
				IndexableField[] index_top = d.getFields("topic");
				for (int j = 0; j < index_top.length; j++) {
					String t = index_top[j].stringValue();
					topics.put(t);
					
				}
				
				visual.put("id",i);
				visual.put("label", d.get("label"));
				 
				//	full_topics.put(topics);
				visual.put("topic", topics);
				
				// Category
				JSONArray cat = new JSONArray();
				IndexableField[] index_cat = d.getFields("category");
				for (int j = 0; j < index_cat.length; j++) {
					cat.put(index_cat[j].stringValue());
				}
				//data.put("categories", cat);
				visual.put("category", cat.get(0));
				// OCURRENCES				
				
				data.put(visual);
		}
		
		System.out.println(data);
		
		
		return data;
	}

}
