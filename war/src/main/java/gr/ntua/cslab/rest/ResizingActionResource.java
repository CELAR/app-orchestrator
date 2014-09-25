
package gr.ntua.cslab.rest;

import gr.ntua.cslab.beans.ResizingAction;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resizing action resource API.
 * @author Giannis Giannakopoulos
 */
@Path("/resizing/")
public class ResizingActionResource {
    
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<ResizingAction> listResizingActions(){
       List<ResizingAction> ret = new LinkedList<>();
       return ret;
    }
    
    @POST
    public Response executeResizingAction(){
       return Response.status(200).build();
    }
    
    @GET
    @Path("status/")
    public Response getResizingActionStatus(@QueryParam("action_id") String actionId){
       return Response.status(200).build();
    }
}
