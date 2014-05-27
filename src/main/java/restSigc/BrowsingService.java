package restSigc;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import lucene.Lucene;


@Produces(MediaType.APPLICATION_JSON)
@Path("/browsing")
public class BrowsingService {
	/*
	@GET
	@Path("/{prop}")
	public Response getProperty( //ServiÃ§o que retorna lista de itens da propriedade
			@PathParam("prop") String prop
			) {
		
			Browsing br = new Browsing();

			try {
				JSONObject res = br.browsing(prop);
				return Response.ok(res).build();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return Response.ok("{}").build();
		
	}*/
	
	/*
	@GET
	@Path("/{prop}/{item}")
	public Response getPropertyItem( //Serviço que retorna lista de artigos do item escolhido
			@PathParam("prop") String prop,
			@PathParam("item") String item
			) {
		
			Browsing br = new Browsing();
		
			try {
				JSONObject res = br.getArticles(prop, item);
				return Response.ok(res).build();
			} catch (IOException e) {
				
			}
		
			
			return Response.ok("{}").build();
		
	}
	*/
	
	
}

