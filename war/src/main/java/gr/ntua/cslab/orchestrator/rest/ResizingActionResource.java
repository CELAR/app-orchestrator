/*
 * Copyright 2014 CELAR.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    public ExecutedResizingAction executeResizingAction(@QueryParam("action_id") int actionId, Parameters params){
        ExecutedResizingAction exec = new ExecutedResizingAction();
        exec.setExecutionStatus(ResizingExecutionStatus.ONGOING);
        exec.setResizingAction(ResizingActionsCache.getResizingActionById(actionId));
        exec.setUniqueId(UUID.randomUUID().toString());
        exec.setParameters(params);
        exec.setTimestamp(System.currentTimeMillis());
        
        ResizingActionsCache.addExecutedResizingAction(exec);          
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