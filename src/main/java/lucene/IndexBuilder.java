package lucene;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import models.Category;
import models.Entity;
import models.NER.Date;
import models.NER.Local;
import models.NER.Money;
import models.NER.Org;
import models.NER.Percent;
import models.NER.Person;
import models.NER.Time;
import models.Topic;
import models.Article;

public class IndexBuilder {
	
	public static void addDocWords(IndexWriter w, Entity e) throws IOException {
		
		Document doc = new Document();
		
		FieldType myField = new FieldType() ;
		
		myField.setIndexed(true);
		myField.setStored(true);
		
		myField.setTokenized(true);
		
		doc.add(new Field("entity", e.getEntity(), myField)); // Entity
		
		doc.add(new TextField("type", e.getType(), Field.Store.YES)); // Entity Type
		
		doc.add(new TextField("count", Integer.toString(e.getOcurrence()), Field.Store.NO)); // Counter
		
		w.addDocument(doc);
	}
	
	
	public static void addDoc(IndexWriter w, Article a) throws IOException {
		Document doc = new Document();
		
		// LABEL - TITLE
		doc.add(new TextField("label", a.getLabel(), Field.Store.YES)); // Label
		
		// DESCRIPTION
		doc.add(new TextField("description", a.getDesc(), Field.Store.YES)); // Description
		
		// PUBLISH_DATE
		doc.add(new TextField("date", a.getPubDate(), Field.Store.YES)); // Description
		
		// ABSTRACT - ARTICLE TEXT CONTENT
		doc.add(new TextField("abstract", a.getAbstract(), Field.Store.YES)); // Abstract 
		
		// TOPICS - MALLET
		//List<Topic> topics = a.getTopics();
		//for(Topic topic : topics){ 
			//doc.add(new TextField("topic", topic.getTopic(), Field.Store.YES));	
		//}
		
		// NAME ENTITY RECOGNITION
		
		// PERSONS
		List<Person> names = a.getPersons();
		if (names != null){
			for(Person name : names){
				doc.add(new TextField("person", name.getPerson(), Field.Store.YES));
			}
		}
		
		// ORGANIZATION
		List<Org> orgs = a.getOrganizations();
		if(orgs != null)
		for(Org org : orgs){ 
			doc.add(new TextField("organization", org.getOrganization(), Field.Store.YES));	
		}
		
		// LOCAL
		List<Local> locals = a.getLocals();
		if(locals != null)
		for(Local local : locals){ 
			doc.add(new TextField("local", local.getLocal() , Field.Store.YES));	
			
		}
		
		// DATE
		List<Date> dates = a.getDates();
		if(dates != null)
		for(Date date : dates){ 
			doc.add(new TextField("date", date.getDate() , Field.Store.YES));	
		}
		
		// MONEY
		List<Money> moneyl = a.getMoneyList();
		if(moneyl != null)
		for(Money money : moneyl){ 
			doc.add(new TextField("money", money.getMoney() , Field.Store.YES));	
		}
		
		// PERCENT
		List<Percent> perc = a.getPercents();
		if(perc != null)
		for(Percent per : perc){ 
			doc.add(new TextField("date", per.getPercent() , Field.Store.YES));	
		}
		
		// TIME
		List<Time> times = a.getTimes();
		if(times != null)
		for(Time time : times){
			doc.add(new StringField("time", time.getTime() , Field.Store.YES));	
		}
		
		// CATEGORY
		List<Category> cats = a.getCategories();
		for(Category cat : cats){ 
				doc.add(new TextField("category", cat.getCategory() , Field.Store.YES));	
		}
		
		//RESOURCE - LINK/SOURCE - ID
		System.out.println(a.getResource());
		doc.add(new TextField("resource", a.getResource(), Field.Store.YES)); // Abstract 

		// SAVE DOC
		w.addDocument(doc);
	}

}
