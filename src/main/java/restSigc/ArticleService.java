package restSigc;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import lucene.Lucene;

@Produces( MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Path("article")
public class ArticleService {
	
	
	@GET
	@Path("/related/{related}")
	public Response getRelated(
			@PathParam("related") String q){
			
			Lucene lu = new Lucene();
			
			try {
				q = new String(q.getBytes("UTF-8"), "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		
			JSONObject search = lu.QuerySearch(q, 0);
		
			return Response.ok(search).build();
	}
	
	@GET
	@Path("{resource}")
	public Response getArticle(
			@PathParam("resource") String resource
			) {
	
			// TODO
			//Sparql sparql = new Sparql();
			Lucene lu = new Lucene();	
			
			
		
			try {
				//JSONObject search = lu.QuerySearch(query,page);
				//JSONObject search = sparql.SparqlSearch(query);
				
				JSONObject search = lu.QuerySearch("resource:\"" + resource + "\"",0);
				
				
				if (search.length() > 0) {

					return Response.ok(search).build();

				} else {

					return Response.ok(new JSONObject()).build();

				}
				
			} catch (Exception e) {
				return Response.ok(new JSONObject()).build();
			}
			
	}	

}
