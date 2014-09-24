
package gr.ntua.cslab.rest;

import gr.ntua.cslab.rest.beans.ResizingActionDetail;
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
public class ResizingAction {
    
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<ResizingActionDetail> listResizingActions(){
       List<ResizingActionDetail> ret = new LinkedList<>();
       ret.add(new ResizingActionDetail(1, "add VM"));
       ret.add(new ResizingActionDetail(2, "remove VM"));
       return ret;
    }
    
    @POST
    public Response executeResizingAction(){
       return Response.status(200).build();
    }
    
    @GET
    @Path("status")
    public Response getResizingActionStatus(@QueryParam("action_id") String actionId){
       return Response.status(200).build();
    }
}
