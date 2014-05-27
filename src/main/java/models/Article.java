package models;

import java.util.List;

import models.NER.Date;
import models.NER.Local;
import models.NER.Money;
import models.NER.Org;
import models.NER.Percent;
import models.NER.Person;
import models.NER.Time;

public class Article {
	
	private String label;				//label
	private String content; 			//abstract
	private String resource; 			//resource
	private String description;			//description
	private String publish_date;		//date
	
	private List<Topic> topics;			//topics
	private List<Category> categories;	//categories
	
	private List<NER.Person> 	persons;			//names
	private List<NER.Org> 	organizations;	//organizations
	private List<NER.Date> dates;			//dates
	private List<NER.Money> moneys;			//money
	private List<NER.Time> 	times;			//times
	private List<NER.Percent> percents;		//percent
	private List<NER.Local> locals;		//percent
	
	private List<IDs> ids;		//ids
	
	public void setResource(String r) {
		this.resource = r;
	}
	
	public String getDesc(){
		return description;
	}
	
	public String getPubDate(){
		return publish_date;
	}

	public String getResource() {
		return resource;
	}	

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	public void setAbstract(String content) {
		this.content = content;
	}

	public String getAbstract() {
		return content;
	}
	
	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}
	
	public List<Topic> getTopics() {
		return topics;
	}

	public List<IDs> getIDS() {
		return ids;
	}
	
	public void setIDs(List<IDs> id) {
		this.ids = id;
	}

	public void setPublishDate(String date){
		this.publish_date = date;
	}
	
	public void setDesc(String d){
		this.description = d;
	}
	
	public void setCategories(List<Category> cat) {
		this.categories = cat;
	}

	public List<Category> getCategories() {
		return categories;
	}
	
	//NER Object Lists
	
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public List<Person> getPersons() {
		return persons;
	}
	
	public void setOrganizations(List<Org> orgs) {
		this.organizations = orgs;
	}

	public List<Org> getOrganizations() {
		return organizations;
	}
	
	public void setLocals(List<Local> locals) {
		this.locals = locals;
	}

	public List<Local> getLocals() {
		return locals;
	}
	
	public void setDates(List<Date> dates) {
		this.dates = dates;
	}

	public List<Date> getDates() {
		return dates;
	}
	
	public void setTimes(List<Time> times) {
		this.times = times;
	}

	public List<Time> getTimes() {
		return times;
	}
	
	public void setPercents(List<Percent> percents) {
		this.percents = percents;
	}

	public List<Percent> getPercents() {
		return percents;
	}
	
	public void setMoneyList(List<Money> moneys) {
		this.moneys = moneys;
	}

	public List<Money> getMoneyList() {
		return moneys;
	}
	
}
