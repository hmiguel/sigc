package stanford;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.DataReader;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import models.Article;
import models.NER;
import models.NER.Local;
import models.NER.Money;
import models.NER.Org;
import models.NER.Person;

public class StringtoNER {
	
	
	public static Article generateNER(Article article,  AbstractSequenceClassifier  classifier){
		                  
        return ExtractXMLTags(article, classifier.classifyWithInlineXML(article.getAbstract()));
			
	}	
    
    @SuppressWarnings("null")
	public static Article ExtractXMLTags(Article article, String xml) {
    
    	String content = article.getAbstract();
     	
    	Pattern ORG = Pattern.compile("<ORGANIZATION>(.+?)</ORGANIZATION>");
    	Pattern PEOPLE = Pattern.compile("<PERSON>(.+?)</PERSON>");
    	Pattern LOC = Pattern.compile("<LOCATION>(.+?)</LOCATION>");
    	Pattern TIME = Pattern.compile("<TIME>(.+?)</TIME>");
    	Pattern MONEY = Pattern.compile("<MONEY>(.+?)</MONEY>");
    	Pattern DATE = Pattern.compile("<DATE>(.+?)</DATE>");
    	Pattern PERCENT = Pattern.compile("<PERCENT>(.+?)</PERCENT>");
    	    	        
        /* Set Persons */
        List<String> people = getTagValues(xml,PEOPLE);
        List<NER.Person> persons = new ArrayList<NER.Person>();
        for (String person: people){
        	Person p = new Person();
        	p.setPerson(person);
        	persons.add(p);
        }
        article.setPersons(persons);
        
        /* Set Organizations */
        List<String> org = getTagValues(xml,ORG);
        List<NER.Org> orgs = new ArrayList<NER.Org>();
        for (String str: org){
        	Org o = new Org();
        	o.setOrganization(str);
        	orgs.add(o);
        }
        article.setOrganizations(orgs);
        
        /* Set Locations */
        List<String> local = getTagValues(xml,LOC);
        
        List<NER.Local> locals = new ArrayList<NER.Local>();
        for (String str: local){
        	Local l = new Local();
        	l.setLocal(str);
        	locals.add(l);
        }
        article.setLocals(locals);
        
        /* Set Money */
        List<String> money = getTagValues(xml,MONEY);
        List<NER.Money> monies = new ArrayList<NER.Money>();
        for (String str: money){
        	Money m = new Money();
        	m.setMoney(str);
        	monies.add(m);
        }
        article.setMoneyList(monies);
        
        /* Set Date */
        List<String> date = getTagValues(xml,DATE);
        List<NER.Date> dates = new ArrayList<NER.Date>();
        for (String str: date){
        	NER.Date d = new NER.Date();
        	d.setDate(str);
        	dates.add(d);
        }
        article.setDates(dates);
        
        /* Set Percent */
        List<String> percent = getTagValues(xml, PERCENT);
        List<NER.Percent> percents = new ArrayList<NER.Percent>();
        for (String str: percent){
        	NER.Percent pc = new NER.Percent();
        	pc.setPercent(str);
        	percents.add(pc);
        }
        article.setDates(dates);
        
        /* Set Time */
        List<String> time = getTagValues(xml, TIME);
        List<NER.Time> times = new ArrayList<NER.Time>();
        for (String str: time){
        	NER.Time t = new NER.Time();
        	t.setTime(str);
        	times.add(t);
        }
        article.setTimes(times);
        
        /* NER DONE */
        
        return article;
        
    }
    

    private static List<String> getTagValues(final String str, Pattern pat) {
        final List<String> tagValues = new ArrayList<String>();
        final Matcher matcher = pat.matcher(str);
        while (matcher.find()) {
            tagValues.add(matcher.group(1));
        }
        return tagValues;
    }
    
    
}
