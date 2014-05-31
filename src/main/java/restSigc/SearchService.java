package restSigc;

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

@Produces(MediaType.APPLICATION_JSON)
@Path("search")
public class SearchService {
	
	
	@GET
	@Path("/query")
	public Response getQueryResults(
			@QueryParam("q") String query,
			@QueryParam("p") int page,
			@Context HttpServletResponse servlerResponse
			) {
	
		servlerResponse.addHeader("Access-Control-Allow-Origin", "*");
	
		// TODO
		Lucene lu = new Lucene();
	
		try {
			JSONObject search = lu.QuerySearch(query,page);
		
			
			if (search.length() > 0) {

				return Response.ok(search).build();

			} else {

				return Response.ok(new JSONObject()).build();

			}
		} catch (Exception e) {
			return Response.ok(new JSONObject()).build();
		}
	}
	
	@GET
	@Path("/suggest")
	public Response getSuggestionResults(
			@QueryParam("term") String word,
			@Context HttpServletResponse servlerResponse
			) {
	
		word = word.toLowerCase();
		
		servlerResponse.addHeader("Access-Control-Allow-Origin", "*");
		
		Lucene lu = new Lucene();
	
		try {
			JSONArray suggestions = lu.suggestTermsFor(word);

			if (suggestions.length() > 0) {

				return Response.ok(suggestions).build();

			} else {

				return Response.ok(new JSONObject()).build();

			}
		} catch (Exception e) {
			return Response.ok(new JSONObject()).build();
		}
	}
	
}
