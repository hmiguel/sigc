package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.codehaus.jettison.json.JSONException;

import models.Article;
import models.Entity;
import models.NER.Time;

public class TT {

	public static void main(String[] args) throws JSONException, IOException {
		// TODO Auto-generated method stub
		
		List<Entity> e = data.DataReader.getEntities();
		
		System.out.println(e.size());
		
		for(Entity some: e){
			System.out.println(some.getEntity() + " " + some.getType() + " " + some.getOcurrence());
		}
	}

}
