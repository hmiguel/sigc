package mallet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cc.mallet.classify.Classifier;
import cc.mallet.pipe.iterator.SimpleFileLineIterator;
import cc.mallet.types.Labeling;

public class Categorization {

	public JSONObject categorize(String data){
		
		JSONObject json = new JSONObject();
		
		//Pre-Processed Classifier File
		if (System.getProperty("os.name").contains("Windows")){
			File file = new File("C:\\Users\\hmiguel\\Desktop\\Faculdade\\2014\\2S\\SIGC-Proj\\classifier.mallet");
		}else{
			
			File file = new File("/home/padsilva/Desktop/SIGC/Classifier/classifier.mallet")
		}
		//Load Classifier
		Classifier clas = getClassifier(file);
					
		//Categorize Article
		List<Ranking> rank = null;
		try {
			rank = CategorizeArticle(clas,data);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject js = new JSONObject();
		
		for(Ranking r: rank){
		
			try {
				js.put(Integer.toString(r.getRank()), r.getCategory().toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return js;
		
	}
	
	public static Classifier getClassifier(File file){
		try {
			return Utils.loadClassifier(file);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return null;
	}
	
	
	public static List<Ranking> CategorizeArticle(Classifier classifier, String data) throws IOException {

		 File temp = null;
		try {
		    // Create temp file.
		    temp = File.createTempFile("pattern", ".suffix");

		    // Delete temp file when program exits.
		    temp.deleteOnExit();

		    // Write to temp file
		    BufferedWriter out = new BufferedWriter(new FileWriter(temp));
		    data = data.replace("\n", "").replace("\r", "");
		    out.write(data);
		    out.close();
		} catch (IOException e) {
		}
		
                                                                  

		SimpleFileLineIterator reader = new SimpleFileLineIterator(temp);
		
        // Create an iterator that will pass each instance through                                         
        //  the same pipe that was used to create the training data                                        
        //  for the classifier.                                                                            
        Iterator<?> instances =
            classifier.getInstancePipe().newIteratorFrom(reader);
        
        List <Ranking> ranking = new ArrayList<Ranking>();
        
        
        // Classifier.classify() returns a Classification object                                           
        //  that includes the instance, the classifier, and the                                            
        //  classification results (the labeling). Here we only                                            
        //  care about the Labeling.                                                                       
        while (instances.hasNext()) {
            Labeling labeling = classifier.classify(instances.next()).getLabeling();

            // print the labels with their weights in descending order (ie best first)                     

            for (int rank = 0; rank < labeling.numLocations(); rank++){
            	Ranking r = new Ranking();
            	r.setCategory(labeling.getLabelAtRank(rank));
            	r.setValue(labeling.getValueAtRank(rank));
            	r.setRank(rank+1);
            	ranking.add(r);
            }

        }
        
        return ranking;
    }
	

}
