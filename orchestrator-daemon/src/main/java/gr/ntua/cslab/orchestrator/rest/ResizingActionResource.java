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

import com.sixsq.slipstream.exceptions.ValidationException;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;
import gr.ntua.cslab.orchestrator.beans.ExecutedResizingAction;
import gr.ntua.cslab.orchestrator.beans.Parameter;
import gr.ntua.cslab.orchestrator.beans.Parameters;
import gr.ntua.cslab.orchestrator.beans.ResizingAction;
import gr.ntua.cslab.orchestrator.beans.ResizingActionList;
import gr.ntua.cslab.orchestrator.beans.ResizingActionType;
import gr.ntua.cslab.orchestrator.cache.ResizingActionsCache;
import gr.ntua.cslab.orchestrator.shared.ServerStaticComponents;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resizing action API.
 * @author Giannis Giannakopoulos
 */
@Path("/resizing/")
public class ResizingActionResource {
    private final static String 
                slipstreamUser = ServerStaticComponents.properties.getProperty("slipstream.username"),
                slipstreamPassword = ServerStaticComponents.properties.getProperty("slipstream.password"),
                slipstreamUrl = "https://"+ServerStaticComponents.properties.getProperty("slipstream.server.host"),
                slipstreamDeploymentId = ServerStaticComponents.properties.getProperty("slipstream.deployment.id");
    
    private static SlipStreamSSService service;

    public ResizingActionResource() throws ValidationException {
         service = new SlipStreamSSService(slipstreamUser, slipstreamPassword, slipstreamUrl);
    }
    
    

    @GET
    public ResizingActionList listResizingActions(){
       return new ResizingActionList(ResizingActionsCache.getAvailalbeResizingActions());
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ExecutedResizingAction executeResizingAction(@QueryParam("action_id") int actionId, Parameters params) throws ValidationException, Exception{
        
        ResizingAction a = ResizingActionsCache.getResizingActionById(actionId);
        
        int multiplicity = 1;
        
        for(Parameter  p : params.getParameters()) {
            if(p.getKey().equals("multiplicity")) 
                multiplicity = new Integer(p.getValue());
        }
        if(a.getType() == ResizingActionType.SCALE_OUT) {
            service.addVM(slipstreamDeploymentId, a.getModuleName(),multiplicity);
        } else if (a.getType() ==  ResizingActionType.SCALE_IN) {
            service.removeVM(slipstreamDeploymentId, a.getModuleName(), multiplicity);
        }
        
        
        
        ExecutedResizingAction exec = new ExecutedResizingAction();
        exec.setExecutionStatus(service.getDeploymentState(slipstreamDeploymentId));
        exec.setResizingAction(ResizingActionsCache.getResizingActionById(actionId));
        exec.setUniqueId(UUID.randomUUID().toString());
        exec.setParameters(params);
        exec.setTimestamp(System.currentTimeMillis());
        
        ResizingActionsCache.addExecutedResizingAction(exec);          
        return exec;
    }
    
    @GET
    @Path("status/")
    public ExecutedResizingAction getResizingActionStatus(@QueryParam("unique_id") String actionId) throws Exception{
        ExecutedResizingAction a = ResizingActionsCache.getExecutedResizingActionByUniqueId(actionId);
        a.setExecutionStatus(service.getDeploymentState(slipstreamDeploymentId));
        if(a!=null)
            return a;
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
}