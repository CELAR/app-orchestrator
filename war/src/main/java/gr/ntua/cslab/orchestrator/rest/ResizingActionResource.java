
package gr.ntua.cslab.orchestrator.rest;

import gr.ntua.cslab.orchestrator.beans.ExecutedResizingAction;
import gr.ntua.cslab.orchestrator.beans.Parameter;
import gr.ntua.cslab.orchestrator.beans.Parameters;
import gr.ntua.cslab.orchestrator.beans.ResizingAction;
import gr.ntua.cslab.orchestrator.beans.ResizingExecutionStatus;
import gr.ntua.cslab.orchestrator.cache.ResizingActionsCache;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
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
       return ResizingActionsCache.getAvailalbeResizingActions();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public ExecutedResizingAction executeResizingAction(@QueryParam("action_id") int actionId
//            Parameters params
    ){
        ExecutedResizingAction exec = new ExecutedResizingAction();
        exec.setExecutionStatus(ResizingExecutionStatus.ONGOING);
        exec.setResizingAction(ResizingActionsCache.getResizingActionById(actionId));
        exec.setUniqueId(UUID.randomUUID().toString());
        
        Parameters parameters = new Parameters();
        parameters.addParameter(new Parameter("cpu", "2"));
        parameters.addParameter(new Parameter("ram", "2"));
        exec.setParameters(parameters);
        ResizingActionsCache.addExecutedResizingAction(exec);
        System.err.println("Wrote action to history");
                
        return exec;
    }
    
    @GET
    @Path("status/")
    public ExecutedResizingAction getResizingActionStatus(@QueryParam("unique_id") String actionId){
        ExecutedResizingAction a = ResizingActionsCache.getExecutedResizingActionByUniqueId(actionId);
        if(a!=null)
            return a;
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
}
