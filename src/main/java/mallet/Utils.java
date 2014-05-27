package mallet;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import cc.mallet.classify.*;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.types.*;
import cc.mallet.classify.tui.Text2Classify;


public class Utils
{
	
	public Classifier trainClassifier(InstanceList trainingInstances) {

        // Here we use a maximum entropy (ie polytomous logistic regression)                               
        //  classifier. Mallet includes a wide variety of classification                                   
        //  algorithms, see the JavaDoc API for details.                                                   

        ClassifierTrainer trainer = new MaxEntTrainer();
        return trainer.train(trainingInstances);
    }
	
	public static void saveClassifier(Classifier classifier, File serializedFile)
	        throws IOException {

	        // The standard method for saving classifiers in                                                   
	        //  Mallet is through Java serialization. Here we                                                  
	        //  write the classifier object to the specified file.                                             

	        ObjectOutputStream oos =
	            new ObjectOutputStream(new FileOutputStream (serializedFile));
	        oos.writeObject (classifier);
	        oos.close();
	    }
	
	public static Classifier loadClassifier(File serializedFile)
	        throws FileNotFoundException, IOException, ClassNotFoundException {

	        // The standard way to save classifiers and Mallet data                                            
	        //  for repeated use is through Java serialization.                                                
	        // Here we load a serialized classifier from a file.                                               

	        Classifier classifier;

	        ObjectInputStream ois =
	            new ObjectInputStream (new FileInputStream (serializedFile));
	        classifier = (Classifier) ois.readObject();
	        ois.close();

	        return classifier;
	  }

}