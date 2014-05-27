
package restSigc;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import mallet.Categorization;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


@Path("news/")
public class CategorizeService {

   
   
    //mvn clean compile exec:java

    @POST
    @Path("categorize/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response categorizeArticle(@FormParam("data") String data) {
    	    	
    	Categorization cat = new Categorization();
       
    	data = data.replace("\n", "").replace("\r", "");
        JSONObject js =  cat.categorize(data);
        
        return Response.ok(js).header("Access-Control-Allow-Origin", "*").build();
    }
    
}
